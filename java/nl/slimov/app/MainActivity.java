package nl.slimov.app;

import android.Manifest;
import android.app.Activity;
import android.os.*;
import android.webkit.*;
import android.view.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.location.*;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.graphics.Color;
import org.json.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPInputStream;

public class MainActivity extends Activity {
 private WebView web;private final ExecutorService pool=Executors.newFixedThreadPool(3);private final ExecutorService streamPool=Executors.newSingleThreadExecutor();
 private final AtomicLong transferred=new AtomicLong();private volatile boolean active=true;private LiveStream stream;private String desiredOwner="";private LocationListener locationListener;
 private final Map<String,byte[]> memory=new ConcurrentHashMap<>();private final Map<String,String> etags=new ConcurrentHashMap<>();private String contact;
 private static final Set<String> OWNERS=new HashSet<>(Arrays.asList("ARR","CXX","EBS","GVB","KEOLIS","QBUZZ","RIG","IVU","OPENOV"));
 @Override public void onCreate(Bundle b){super.onCreate(b);contact=BuildConfig.CONTACT;
  try{HttpResponseCache.install(new File(getCacheDir(),"http"),48L*1024*1024);}catch(Exception ignored){}
  getWindow().setStatusBarColor(Color.parseColor("#FAF8FF"));getWindow().setNavigationBarColor(Color.parseColor("#FAF8FF"));
  web=new WebView(this);web.setBackgroundColor(Color.parseColor("#FAF8FF"));web.setOverScrollMode(View.OVER_SCROLL_NEVER);android.widget.FrameLayout root=new android.widget.FrameLayout(this);root.addView(web,new android.widget.FrameLayout.LayoutParams(-1,-1));setContentView(root);
  root.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets bars=insets.getInsets(WindowInsets.Type.systemBars()|WindowInsets.Type.displayCutout()|WindowInsets.Type.ime());v.setPadding(bars.left,bars.top,bars.right,bars.bottom);return insets;});
  root.setBackgroundColor(Color.parseColor("#FAF8FF"));
  if(Build.VERSION.SDK_INT>=33)getOnBackInvokedDispatcher().registerOnBackInvokedCallback(android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,()->web.evaluateJavascript("window.appBack&&window.appBack()",null));
  WebSettings settings=web.getSettings();settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);settings.setAllowFileAccess(false);settings.setAllowContentAccess(false);settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);settings.setMediaPlaybackRequiresUserGesture(true);settings.setUserAgentString("SlimOV/0.3.0 ("+contact+")");settings.setCacheMode(WebSettings.LOAD_DEFAULT);
  web.addJavascriptInterface(new Bridge(),"Android");web.setWebChromeClient(new WebChromeClient());
  web.setWebViewClient(new WebViewClient(){
   @Override public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest req){Uri u=req.getUrl();if("file".equals(u.getScheme())&&u.getPath()!=null&&u.getPath().startsWith("/android_asset/"))return false;if("https".equals(u.getScheme()))openExternal(u.toString());return true;}
   @Override public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest req){String host=req.getUrl().getHost();if(host!=null&&(host.equals("tile.openstreetmap.org")||host.endsWith(".tile.openstreetmap.org"))){try{byte[] bytes=fetch(req.getUrl().toString(),2*1024*1024,false);return new WebResourceResponse("image/png",null,new ByteArrayInputStream(bytes));}catch(Exception e){return new WebResourceResponse("image/png",null,404,"Unavailable",Collections.emptyMap(),new ByteArrayInputStream(new byte[0]));}}return null;}
  });
  web.loadUrl("file:///android_asset/index.html");
 }
 private void event(String name,JSONObject value){runOnUiThread(()->{if(web!=null)web.evaluateJavascript("window.nativeEvent&&window.nativeEvent("+JSONObject.quote(name)+","+value.toString()+")",null);});}
 private void result(String id,Object value,String error){try{event("result",new JSONObject().put("id",id).put("data",value==null?JSONObject.NULL:value).put("error",error==null?JSONObject.NULL:error));}catch(Exception ignored){}}
 private byte[] fetch(String url,int limit,boolean conditional)throws Exception {
  if(!active)throw new IOException("App is gepauzeerd");HttpURLConnection c=(HttpURLConnection)new URL(url).openConnection();c.setConnectTimeout(12000);c.setReadTimeout(18000);c.setInstanceFollowRedirects(false);c.setRequestProperty("User-Agent","SlimOV/0.3.0 ("+contact+")");c.setRequestProperty("Accept-Encoding","gzip");
  if(conditional&&etags.containsKey(url))c.setRequestProperty("If-None-Match",etags.get(url));
  try{int status=c.getResponseCode();if(status==304&&memory.containsKey(url))return memory.get(url);if(status!=200)throw new IOException(status==429?"Databron vraagt om rust. Probeer het later opnieuw.":"Databron niet beschikbaar (HTTP "+status+")");
   InputStream raw=c.getInputStream();ByteArrayOutputStream compressed=new ByteArrayOutputStream();byte[] buf=new byte[8192];int n;while((n=raw.read(buf))!=-1){if(compressed.size()+n>limit)throw new IOException("Antwoord te groot");compressed.write(buf,0,n);}byte[] bytes=compressed.toByteArray();String cache=c.getHeaderField("X-Android-Response-Source");if(cache==null||!cache.startsWith("CACHE"))transferred.addAndGet(bytes.length);
   if("gzip".equalsIgnoreCase(c.getContentEncoding()))bytes=LiveStream.readBounded(new GZIPInputStream(new ByteArrayInputStream(bytes)),limit*8);
   if(conditional){memory.put(url,bytes);String tag=c.getHeaderField("ETag");if(tag!=null)etags.put(url,tag);}return bytes;
  }finally{c.disconnect();}
 }
 private String q(String s)throws Exception{return URLEncoder.encode(s,"UTF-8");}
 private JSONObject object(byte[] b)throws Exception{return new JSONObject(new String(b,StandardCharsets.UTF_8));}
 private void openExternal(String url){try{Uri u=Uri.parse(url);if(!"https".equals(u.getScheme()))return;startActivity(new Intent(Intent.ACTION_VIEW,u));}catch(Exception ignored){}}
 private void stopStream(){if(stream!=null){stream.stop();stream=null;}}
 private void changeStream(String owner){desiredOwner=OWNERS.contains(owner)?owner:"";streamPool.execute(()->{stopStream();String chosen=desiredOwner;if(!active||chosen.isEmpty())return;stream=new LiveStream(chosen,new LiveStream.Listener(){public void positions(JSONArray points,int bytes){transferred.addAndGet(bytes);try{event("positions",new JSONObject().put("positions",points).put("owner",chosen));}catch(Exception ignored){}}public void status(String text){try{event("stream",new JSONObject().put("message",text).put("owner",chosen));}catch(Exception ignored){}}});stream.start();});}
 public class Bridge {
  @JavascriptInterface public void request(String id,String kind,String raw){pool.execute(()->{try{JSONObject p=new JSONObject(raw);Object response;switch(kind){
   case "vehicles":response=Proto.vehicles(fetch("https://gtfs.ovapi.nl/nl/vehiclePositions.pb",3*1024*1024,true));break;
   case "alerts":response=Proto.alerts(fetch("https://gtfs.ovapi.nl/nl/alerts.pb",6*1024*1024,true));break;
   case "departures":response=object(fetch("https://api.transitous.org/api/v6/stoptimes?stopId="+q(p.optString("stopId"))+"&center="+q(p.getDouble("lat")+","+p.getDouble("lon"))+"&radius=70&n=35&language=nl",3*1024*1024,false));break;
   case "plan":String planUrl="https://api.transitous.org/api/v6/plan?fromPlace="+q(p.getString("from"))+"&toPlace="+q(p.getString("to"))+"&numItineraries=4&language=nl&detailedTransfers=true&joinInterlinedLegs=false&arriveBy="+p.optBoolean("arriveBy");for(String key:new String[]{"time","maxTransfers","additionalTransferTime","transitModes","via"})if(p.has(key)&&!p.optString(key).isEmpty())planUrl+="&"+key+"="+q(p.optString(key));response=object(fetch(planUrl,5*1024*1024,false));break;
   case "trip":response=object(fetch("https://api.transitous.org/api/v6/trip?tripId="+q(p.getString("tripId"))+"&language=nl&detailedLegs="+p.optBoolean("detailed",true)+"&withScheduledSkippedStops=true&joinInterlinedLegs=false",5*1024*1024,false));break;
   case "geocode":response=new JSONArray(new String(fetch("https://api.transitous.org/api/v1/geocode?text="+q(p.getString("text"))+"&language=nl",2*1024*1024,false),StandardCharsets.UTF_8));break;
   case "ovstop":String code=p.getString("code");if(!code.matches("[A-Za-z0-9_-]{1,25}"))throw new Exception("Ongeldige haltecode");response=object(fetch("http://v0.ovapi.nl/tpc/"+code,3*1024*1024,false));break;
   case "stats":int uid=android.os.Process.myUid();response=new JSONObject().put("bytes",transferred.get()).put("uidRx",android.net.TrafficStats.getUidRxBytes(uid)).put("uidTx",android.net.TrafficStats.getUidTxBytes(uid)).put("version","0.3.0").put("contact",contact);break;
   default:throw new Exception("Onbekend verzoek");
  }result(id,response,null);}catch(Exception e){result(id,null,e.getMessage()==null?"Verbinding niet beschikbaar":e.getMessage());}});}
  @JavascriptInterface public void stream(String owner){changeStream(owner);}
  @JavascriptInterface public void theme(boolean dark){runOnUiThread(()->{int color=Color.parseColor(dark?"#101012":"#FAF8FF");((View)web.getParent()).setBackgroundColor(color);web.setBackgroundColor(color);getWindow().setStatusBarColor(color);getWindow().setNavigationBarColor(color);getWindow().getDecorView().setSystemUiVisibility(dark?0:View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);});}
  @JavascriptInterface public void exit(){runOnUiThread(()->moveTaskToBack(true));}
  @JavascriptInterface public void locate(){runOnUiThread(()->requestLocation());}
  @JavascriptInterface public void external(String url){runOnUiThread(()->openExternal(url));}
  @JavascriptInterface public void keepAwake(boolean enabled){runOnUiThread(()->{if(enabled&&active)getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);else getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);});}
  @JavascriptInterface public void share(String text){runOnUiThread(()->{try{Intent send=new Intent(Intent.ACTION_SEND);send.setType("text/plain");send.putExtra(Intent.EXTRA_TEXT,text.substring(0,Math.min(text.length(),12000)));startActivity(Intent.createChooser(send,"Deel je SlimOV-reis"));}catch(Exception ignored){}});}
  @JavascriptInterface public void enableNotifications(){runOnUiThread(()->{if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},22);});}
  @JavascriptInterface public void notify(String title,String body){runOnUiThread(()->{if(!active)return;try{android.app.NotificationManager manager=getSystemService(android.app.NotificationManager.class);manager.createNotificationChannel(new android.app.NotificationChannel("journey","Uitstapherinneringen",android.app.NotificationManager.IMPORTANCE_HIGH));if(!manager.areNotificationsEnabled()){event("notice",new JSONObject().put("message","Meldingen zijn niet toegestaan. De herinnering staat alleen in SlimOV."));return;}Intent open=new Intent(MainActivity.this,MainActivity.class);android.app.PendingIntent pending=android.app.PendingIntent.getActivity(MainActivity.this,0,open,android.app.PendingIntent.FLAG_UPDATE_CURRENT|android.app.PendingIntent.FLAG_IMMUTABLE);manager.notify(9,new android.app.Notification.Builder(MainActivity.this,"journey").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(title).setContentText(body).setStyle(new android.app.Notification.BigTextStyle().bigText(body)).setContentIntent(pending).setAutoCancel(true).build());}catch(Exception ignored){}});}
 }
 private void requestLocation(){if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},21);return;}
  try{LocationManager manager=(LocationManager)getSystemService(LOCATION_SERVICE);if(locationListener!=null)manager.removeUpdates(locationListener);locationListener=new LocationListener(){public void onLocationChanged(Location l){manager.removeUpdates(this);try{event("location",new JSONObject().put("lat",l.getLatitude()).put("lon",l.getLongitude()).put("accuracy",l.getAccuracy()));}catch(Exception ignored){}}};String provider=manager.isProviderEnabled(LocationManager.GPS_PROVIDER)?LocationManager.GPS_PROVIDER:LocationManager.NETWORK_PROVIDER;manager.requestLocationUpdates(provider,1000,10,locationListener);new Handler(Looper.getMainLooper()).postDelayed(()->{if(locationListener!=null)manager.removeUpdates(locationListener);},20000);
  }catch(Exception e){try{event("notice",new JSONObject().put("message","Locatie niet beschikbaar. Controleer de locatiestand van je telefoon."));}catch(Exception ignored){}}
 }
 @Override public void onRequestPermissionsResult(int request,String[] permissions,int[] grants){super.onRequestPermissionsResult(request,permissions,grants);if(request==21){if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)requestLocation();else try{event("notice",new JSONObject().put("message","Locatie niet toegestaan. Je kunt gewoon op een halte zoeken."));}catch(Exception ignored){}}}
 @Override protected void onPause(){active=false;getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);streamPool.execute(()->stopStream());if(web!=null){web.evaluateJavascript("window.appPause&&window.appPause()",null);web.onPause();}LocationManager m=(LocationManager)getSystemService(LOCATION_SERVICE);if(locationListener!=null)m.removeUpdates(locationListener);super.onPause();}
 @Override protected void onResume(){super.onResume();active=true;if(web!=null){web.onResume();web.evaluateJavascript("window.appResume&&window.appResume()",null);}if(!desiredOwner.isEmpty())changeStream(desiredOwner);}
 @Override protected void onDestroy(){active=false;streamPool.execute(()->stopStream());pool.shutdownNow();streamPool.shutdown();if(web!=null){web.removeJavascriptInterface("Android");web.destroy();web=null;}super.onDestroy();}
 @Override public void onBackPressed(){web.evaluateJavascript("window.appBack&&window.appBack()",null);}
}
