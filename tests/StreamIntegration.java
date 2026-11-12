import nl.slimov.app.LiveStream;
import org.json.*;
import java.util.concurrent.atomic.*;
public class StreamIntegration {
 public static void main(String[] args)throws Exception {AtomicInteger points=new AtomicInteger(),bytes=new AtomicInteger();AtomicLong freshest=new AtomicLong();LiveStream live=new LiveStream("GVB",new LiveStream.Listener(){public void positions(JSONArray a,int size){bytes.addAndGet(size);points.addAndGet(a.length());for(int i=0;i<a.length();i++)freshest.accumulateAndGet(a.getJSONObject(i).getLong("timestamp"),Math::max);}public void status(String s){System.out.println(s);}});live.start();Thread.sleep(12000);live.stop();int count=points.get(),received=bytes.get();if(count<1)throw new AssertionError("No decoded live points received");System.out.println("PASS direct foreground stream: "+count+" decoded positions, "+received+" bytes, newest position age "+(System.currentTimeMillis()/1000-freshest.get())+" seconds");int before=bytes.get();Thread.sleep(1200);if(bytes.get()!=before)throw new AssertionError("Stream continued after stop");System.out.println("PASS stream stops receiving after lifecycle stop");}
}
