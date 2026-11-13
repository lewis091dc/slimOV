import nl.slimov.app.WatchLogic;import nl.slimov.app.TrainApi;import org.json.*;
public class WatchLogicTest {
 static int count;static void ok(boolean b,String n){if(!b)throw new AssertionError(n);count++;System.out.println("PASS "+n);}
 public static void main(String[] args)throws Exception{
  JSONObject model=new JSONObject("{points:[[52,5],[52,5.01]],cumulative:[0,685],stops:[{id:'a',lat:52,lon:5,along:0},{id:'b',lat:52,lon:5.01,along:685}]}");
  JSONObject p=new JSONObject().put("lat",52).put("lon",5.005).put("timestamp",System.currentTimeMillis()/1000);
  ok(WatchLogic.progress(model,p,-1).optBoolean("reliable"),"fresh position advances between stops");
  ok(WatchLogic.progress(model,p,-1).getInt("next")==1,"next stop uses route order");
  p.put("positionTimeKnown",false);ok(!WatchLogic.progress(model,p,-1).optBoolean("reliable"),"received time cannot substitute unknown measurement time");p.put("positionTimeKnown",true).put("timestamp",System.currentTimeMillis()/1000-121);ok(!WatchLogic.progress(model,p,-1).optBoolean("reliable"),"stale GPS cannot trigger progress");
  p.put("timestamp",System.currentTimeMillis()/1000).put("lat",53);ok(!WatchLogic.progress(model,p,-1).optBoolean("reliable"),"off-route GPS rejected");
  JSONObject event=new JSONObject("{legs:[{tripId:'x',from:{stopId:'a',scheduledDeparture:'2026-11-13T10:00:00Z'},to:{stopId:'b'}}]}");ok(WatchLogic.event(event,"x","a","2026-11-13T11:00:00Z","departure")==null,"different scheduled stop event rejected");ok(WatchLogic.event(event,"other","a","","departure")==null,"other trip cannot supply history");
  JSONObject trains=new JSONObject("{payload:{treinen:[{ritId:'composite-id',treinNummer:123,lat:52,lng:5},{ritId:999,lat:0,lng:0}]}}");JSONArray data=TrainApi.positions(trains);ok(data.length()==1,"invalid train coordinates excluded; numeric service fallback accepted");ok(!data.getJSONObject(0).getBoolean("positionTimeKnown"),"train fetch time is not claimed as GPS timestamp");ok(data.getJSONObject(0).getString("number").isEmpty(),"train service number is not a stock number");
  boolean unknown=false;try{TrainApi.positions(new JSONObject("{payload:{unexpected:[]}}"));}catch(Exception e){unknown=e.getMessage().contains("antwoordformaat");}ok(unknown,"unknown NS schema is an actionable error, not an empty successful map");
  ok(TrainApi.positions(new JSONObject("{payload:{treinen:[]}}")).length()==0,"valid empty source is distinguished from unknown schema");
  System.out.println("TOTAL "+count+" checks");
 }
}
