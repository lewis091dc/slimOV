import nl.slimov.app.Proto;
import nl.slimov.app.LiveStream;
import org.json.*;
import java.nio.file.*;
import java.nio.*;
import java.io.*;
import java.util.zip.*;
public class DecoderTest {
 static int passed=0;static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);passed++;System.out.println("PASS "+label);}
 static byte[] var(long n){ByteArrayOutputStream b=new ByteArrayOutputStream();do{int x=(int)n&127;n>>>=7;b.write(x|(n!=0?128:0));}while(n!=0);return b.toByteArray();}
 static byte[] field(int k,int wire,byte[] v)throws Exception {ByteArrayOutputStream b=new ByteArrayOutputStream();b.write(var(k*8+wire));if(wire==2)b.write(var(v.length));b.write(v);return b.toByteArray();}
 static byte[] join(byte[]...a)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();for(byte[] x:a)b.write(x);return b.toByteArray();}
 public static void main(String[] args)throws Exception{
  JSONObject data=Proto.vehicles(Files.readAllBytes(Paths.get(args[0])));JSONArray vehicles=data.getJSONArray("vehicles");check(vehicles.length()>100,"real national vehicle feed decoded");check(data.getLong("timestamp")>1700000000,"feed timestamp preserved");JSONObject v=vehicles.getJSONObject(0);check(v.has("route")&&v.has("number")&&v.has("realtimeId"),"route, operator journey reference and vehicle number preserved");
  byte[] pos=join(field(1,5,ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(52f).array()),field(2,5,ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(5f).array()));byte[] ext=field(1,0,var(-120));byte[] veh=join(field(2,2,pos),field(1003,2,ext),field(5,0,var(1794500000)));byte[] feed=field(2,2,field(4,2,veh));check(Proto.vehicles(feed).getJSONArray("vehicles").getJSONObject(0).getInt("delay")==-120,"negative int32 delay stays negative (early bus)");
  boolean rejected=false;try{Proto.vehicles(new byte[]{18,(byte)255,127});}catch(Exception e){rejected=true;}check(rejected,"truncated/malicious protobuf length rejected");
  String xml="<?xml version=\"1.0\"?><t:VV_TM_PUSH xmlns:t=\"urn:kv6\"><t:KV6posinfo><t:ONROUTE><t:dataownercode>GVB</t:dataownercode><t:lineplanningnumber>1</t:lineplanningnumber><t:journeynumber>2000</t:journeynumber><t:vehiclenumber>2020</t:vehiclenumber><t:timestamp>2026-11-12T19:43:23.7873085+01:00</t:timestamp><t:punctuality>-96</t:punctuality><t:rd-x>114293</t:rd-x><t:rd-y>485525</t:rd-y></t:ONROUTE></t:KV6posinfo></t:VV_TM_PUSH>";ByteArrayOutputStream buffer=new ByteArrayOutputStream();try(GZIPOutputStream gzip=new GZIPOutputStream(buffer)){gzip.write(xml.getBytes("UTF-8"));}JSONArray points=LiveStream.parse(buffer.toByteArray());check(points.length()==1,"namespaced gzip KV6 message decoded");JSONObject p=points.getJSONObject(0);check(p.getInt("delay")==-96&&p.getString("vehiclenumber").equals("2020"),"KV6 early-running and physical vehicle number preserved");check(p.getDouble("lat")>52.35&&p.getDouble("lat")<52.38&&p.getDouble("lon")>4.78&&p.getDouble("lon")<4.81,"RD coordinates transformed to Amsterdam WGS84 position");
  double[] ref=LiveStream.rdToWgs(155000,463000);check(Math.abs(ref[0]-52.1551744)<1e-8&&Math.abs(ref[1]-5.38720621)<1e-8,"RD reference point matches published origin");check(Proto.alerts(Files.readAllBytes(Paths.get(args[1]))).has("alerts"),"real service alerts feed decoded");System.out.println("TOTAL "+passed+" checks passed; "+vehicles.length()+" live vehicles");
 }
}
