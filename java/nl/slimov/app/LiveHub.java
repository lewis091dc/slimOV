package nl.slimov.app;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;
/** All screens/services share a single physical NDOV socket, including during handoff. */
public final class LiveHub {
 private static final Map<String,Entry> clients=new ConcurrentHashMap<>();
 private static final ExecutorService serial=Executors.newSingleThreadExecutor();private static LiveStream stream;private static String owners="";private static long generation;
 private static class Entry {String owner;LiveStream.Listener listener;Entry(String o,LiveStream.Listener l){owner=o;listener=l;}}
 public static void set(String id,String owner,LiveStream.Listener listener){if(owner==null||owner.isEmpty()||listener==null)clients.remove(id);else clients.put(id,new Entry(owner,listener));serial.execute(()->rebuild());}
 private static void rebuild(){TreeSet<String> wanted=new TreeSet<>();for(Entry e:clients.values())wanted.add(e.owner);String next=String.join(",",wanted);if(next.equals(owners))return;generation++;long g=generation;if(stream!=null){stream.stop();stream=null;}owners=next;if(next.isEmpty())return;stream=new LiveStream(next,new LiveStream.Listener(){public void positions(JSONArray points,int bytes){if(g!=generation)return;for(Entry e:clients.values()){JSONArray selected=new JSONArray();for(int i=0;i<points.length();i++){JSONObject p=points.optJSONObject(i);if(p!=null&&belongs(e.owner,p.optString("dataownercode")))selected.put(p);}if(selected.length()>0)e.listener.positions(selected,bytes);}}public void status(String s){if(g==generation)for(Entry e:clients.values())e.listener.status(s);}});stream.start();}
 public static boolean belongs(String owner,String dataOwner){return owner.equals(dataOwner)||owner.equals("RIG")&&(dataOwner.equals("RET")||dataOwner.equals("HTM"));}
}
