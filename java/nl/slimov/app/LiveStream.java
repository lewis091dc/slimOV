package nl.slimov.app;

import org.zeromq.*;
import org.json.*;
import org.xmlpull.v1.*;
import android.util.Xml;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.time.OffsetDateTime;

/** A single, foreground-only NDOV subscription. No intermediate server. */
public final class LiveStream implements Runnable {
 public interface Listener {void positions(JSONArray a,int bytes);void status(String s);}
 private final Listener listener;private final String[] topics;private volatile boolean running=true;private Thread thread;
 public LiveStream(String owner,Listener listener){this.listener=listener;this.topics=Arrays.stream(owner.split(",")).filter(v->!v.isEmpty()).map(v->"/"+v+"/KV6posinfo").toArray(String[]::new);}
 public void start(){thread=new Thread(this,"SlimOV-live");thread.start();}
 public void stop(){running=false;if(thread!=null)try{thread.join(900);}catch(InterruptedException ignored){Thread.currentThread().interrupt();}}
 @Override public void run(){try(ZContext ctx=new ZContext()){ZMQ.Socket socket=ctx.createSocket(SocketType.SUB);socket.setLinger(0);socket.setReceiveTimeOut(500);socket.setRcvHWM(25);for(String topic:topics)socket.subscribe(topic);socket.connect("tcp://pubsub.besteffort.ndovloket.nl:7658");listener.status("Verbinden met livefeed…");long last=System.currentTimeMillis();
  while(running){byte[] envelope=socket.recv();if(envelope==null){if(System.currentTimeMillis()-last>20000){listener.status("Nog geen nieuwe voertuigmelding");last=System.currentTimeMillis();}continue;}while(socket.hasReceiveMore()){byte[] payload=socket.recv();if(!running)break;try{JSONArray points=parse(payload);listener.positions(points,payload.length+envelope.length);last=System.currentTimeMillis();}catch(Exception e){listener.status("Een livebericht kon niet worden gelezen");}}}
 }catch(Exception e){if(running)listener.status("Liveverbinding niet beschikbaar; laatste posities blijven staan");}}
 public static JSONArray parse(byte[] data)throws Exception {
  InputStream in=new ByteArrayInputStream(data);if(data.length>2&&(data[0]&255)==31&&(data[1]&255)==139)in=new GZIPInputStream(in);
  // Tolerate a BOM and a stray leading byte used by some operator feeds.
  byte[] bytes=readBounded(in,4*1024*1024);String xml=new String(bytes,"UTF-8");int first=xml.indexOf('<');if(first<0)return new JSONArray();xml=xml.substring(first);
  XmlPullParser parser=Xml.newPullParser();parser.setInput(new StringReader(xml));JSONArray out=new JSONArray();Map<String,String> point=null;String key=null;int event;
  Set<String> events=new HashSet<>(Arrays.asList("ONROUTE","ONSTOP","ARRIVAL","DEPARTURE","OFFROUTE","INIT","END"));
  while((event=parser.next())!=XmlPullParser.END_DOCUMENT){String name=parser.getName();if(name!=null&&name.contains(":"))name=name.substring(name.indexOf(':')+1);
   if(event==XmlPullParser.START_TAG){if(events.contains(name)){point=new HashMap<>();point.put("event",name);}else if(point!=null)key=name;}
   else if(event==XmlPullParser.TEXT&&point!=null&&key!=null)point.put(key,parser.getText());
   else if(event==XmlPullParser.END_TAG){if(events.contains(name)&&point!=null){try{int x=Integer.parseInt(point.getOrDefault("rd-x","-1")),y=Integer.parseInt(point.getOrDefault("rd-y","-1"));if(x>0&&y>0){double[] ll=rdToWgs(x,y);JSONObject p=new JSONObject();for(String k:new String[]{"dataownercode","lineplanningnumber","journeynumber","vehiclenumber","event","userstopcode","passagesequencenumber"})p.put(k,point.getOrDefault(k,""));p.put("lat",ll[0]);p.put("lon",ll[1]);p.put("timestamp",OffsetDateTime.parse(point.get("timestamp")).toEpochSecond());if(point.containsKey("punctuality"))p.put("delay",Integer.parseInt(point.get("punctuality")));out.put(p);}}catch(Exception ignored){}point=null;}key=null;}
  }return out;
 }
 public static byte[] readBounded(InputStream in,int max)throws IOException {ByteArrayOutputStream out=new ByteArrayOutputStream();byte[] b=new byte[8192];int n;while((n=in.read(b))!=-1){if(out.size()+n>max)throw new IOException("Response too large");out.write(b,0,n);}return out.toByteArray();}
 public static double[] rdToWgs(double x,double y){double dx=(x-155000)*1e-5,dy=(y-463000)*1e-5;
  double lat=52.15517440+(3235.65389*dy-32.58297*dx*dx-0.2475*dy*dy-0.84978*dx*dx*dy-0.0655*Math.pow(dy,3)-0.01709*dx*dx*dy*dy-0.00738*dx+0.0053*Math.pow(dx,4)-0.00039*dx*dx*Math.pow(dy,3)+0.00033*Math.pow(dx,4)*dy-0.00012*dx*dy)/3600;
  double lon=5.38720621+(5260.52916*dx+105.94684*dx*dy+2.45656*dx*dy*dy-0.81885*Math.pow(dx,3)+0.05594*dx*Math.pow(dy,3)-0.05607*Math.pow(dx,3)*dy+0.01199*dy-0.00256*Math.pow(dx,3)*dy*dy+0.00128*dx*Math.pow(dy,4)+0.00022*dy*dy-0.00022*dx*dx+0.00026*Math.pow(dx,5))/3600;
  return new double[]{lat,lon};
 }
}
