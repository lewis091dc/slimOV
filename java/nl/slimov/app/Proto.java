package nl.slimov.app;

import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

/** Minimal bounded decoder for the public GTFS-Realtime wire format. */
public final class Proto {
 public static class Message {
  final Map<Integer,List<Object>> values=new HashMap<>();
  Message(byte[] bytes) throws Exception {
   ByteBuffer b=ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
   while(b.hasRemaining()) {long tag=varint(b);int key=(int)(tag>>>3),wire=(int)tag&7;if(key==0)throw new Exception("Invalid protobuf tag");Object v;
    switch(wire){case 0:v=varint(b);break;case 1:v=b.getDouble();break;case 2:long size=varint(b);if(size<0||size>b.remaining())throw new Exception("Invalid protobuf length");byte[] data=new byte[(int)size];b.get(data);v=data;break;case 5:v=(double)b.getFloat();break;default:throw new Exception("Unsupported protobuf wire type");}
    values.computeIfAbsent(key,k->new ArrayList<>()).add(v);
   }
  }
  static long varint(ByteBuffer b)throws Exception {long v=0;for(int s=0;s<64;s+=7){int n=b.get()&255;v|=(long)(n&127)<<s;if((n&128)==0)return v;}throw new Exception("Invalid protobuf varint");}
  boolean has(int k){return values.containsKey(k);}
  Object get(int k){return has(k)?values.get(k).get(0):null;}
  String str(int k){Object o=get(k);return o instanceof byte[]?new String((byte[])o,StandardCharsets.UTF_8):"";}
  long num(int k){Object o=get(k);return o instanceof Number?((Number)o).longValue():0;}
  double decimal(int k){Object o=get(k);return o instanceof Number?((Number)o).doubleValue():0;}
  Message msg(int k)throws Exception {Object o=get(k);return new Message(o instanceof byte[]?(byte[])o:new byte[0]);}
  List<Object> all(int k){return values.getOrDefault(k,Collections.emptyList());}
 }
 public static JSONObject vehicles(byte[] data)throws Exception {
  Message feed=new Message(data);JSONArray out=new JSONArray();
  for(Object raw:feed.all(2)){Message entity=new Message((byte[])raw);if(!entity.has(4)||entity.num(2)==1)continue;Message v=entity.msg(4),p=v.msg(2),t=v.msg(1),desc=v.msg(8);double lat=p.decimal(1),lon=p.decimal(2);if(lat<50.7||lat>53.7||lon<3.2||lon>7.3)continue;
   JSONObject x=new JSONObject();x.put("id",entity.str(1));x.put("trip",t.str(1));x.put("route",t.str(5));x.put("startDate",t.str(3));x.put("startTime",t.str(2));x.put("direction",t.has(6)?t.num(6):-1);x.put("lat",lat);x.put("lon",lon);x.put("timestamp",v.num(5));x.put("number",desc.str(2).isEmpty()?desc.str(1):desc.str(2));x.put("realtimeId",t.msg(1003).str(1));x.put("stop",v.str(7));if(v.msg(1003).has(1))x.put("delay",(int)v.msg(1003).num(1));out.put(x);
  }
  return new JSONObject().put("timestamp",feed.msg(1).num(3)).put("vehicles",out);
 }
 private static String translated(Message m)throws Exception {String first="";for(Object raw:m.all(1)){Message t=new Message((byte[])raw);if(first.isEmpty())first=t.str(1);if(t.str(2).startsWith("nl"))return t.str(1);}return first;}
 public static JSONObject alerts(byte[] data)throws Exception {
  Message feed=new Message(data);JSONArray out=new JSONArray();long now=System.currentTimeMillis()/1000;
  for(Object raw:feed.all(2)){Message e=new Message((byte[])raw);if(!e.has(5)||e.num(2)==1)continue;Message a=e.msg(5);JSONArray routes=new JSONArray(),stops=new JSONArray(),agencies=new JSONArray();boolean active=a.all(1).isEmpty();
   for(Object r:a.all(1)){Message period=new Message((byte[])r);if((!period.has(1)||period.num(1)<=now)&&(!period.has(2)||period.num(2)>=now))active=true;}
   if(!active)continue;
   for(Object r:a.all(5)){Message selector=new Message((byte[])r);if(!selector.str(2).isEmpty())routes.put(selector.str(2));if(!selector.str(4).isEmpty())stops.put(selector.str(4));if(!selector.str(1).isEmpty())agencies.put(selector.str(1));}
   out.put(new JSONObject().put("id",e.str(1)).put("title",translated(a.msg(10))).put("description",translated(a.msg(11))).put("url",translated(a.msg(8))).put("effect",a.num(7)).put("routes",routes).put("stops",stops).put("agencies",agencies));
  }
  return new JSONObject().put("timestamp",feed.msg(1).num(3)).put("alerts",out);
 }
}
