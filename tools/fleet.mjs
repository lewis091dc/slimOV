import fs from 'node:fs';
import zlib from 'node:zlib';
const base=process.argv[2]||'build/fleet-input';
const sourceFiles=JSON.parse(fs.readFileSync(base+'/sources.json','utf8'));
function unescape(s){return s.replace(/&amp;/g,'&').replace(/&lt;/g,'<').replace(/&gt;/g,'>').replace(/&quot;/g,'"').replace(/&apos;/g,"'")}
const tag=(s,name)=>unescape(s.match(new RegExp('<'+name+'(?:\\s[^>]*)?>([^<]*)</'+name+'>'))?.[1]||'');
const attr=(s,name)=>unescape(s.match(new RegExp('\\b'+name+'="([^"]*)"'))?.[1]||'');
let fleet={};let sources={};const asOf=new Date().toISOString().slice(0,10);
for(const source of sourceFiles){let xml=zlib.gunzipSync(fs.readFileSync(base+'/'+source.file)).toString();const types=new Map();for(let match of xml.matchAll(/<VehicleType\b[\s\S]*?<\/VehicleType>/g)){let block=match[0];types.set(attr(block,'id'),{name:tag(block,'Name'),fuel:tag(block,'FuelType'),length:tag(block,'Length')})}let count=0;for(let match of xml.matchAll(/<Vehicle\b[\s\S]*?<\/Vehicle>/g)){let block=match[0],from=tag(block,'FromDate'),to=tag(block,'ToDate');if(from&&new Date(from)>new Date('2026-11-12T12:00:00Z')||to&&new Date(to)<new Date('2026-11-12T12:00:00Z'))continue;let ref=block.match(/<VehicleTypeRef\b[^>]*>/)?.[0]||'',type=types.get(attr(ref,'ref'));if(!type)continue;let number=tag(block,'OperationalNumber')||block.match(/<PrivateCode type="VehicleNumber">([^<]*)/)?.[1];if(!number)continue;let owner=source.owner.toUpperCase(),name=type.name,make=name.match(/\b(BYD|VDL|Ebusco|Heuliez|Iveco|Mercedes(?:-Benz)?|Solaris|Volvo|MAN|Scania|Alexander Dennis)\b/i)?.[1]||'';let id=owner+':'+number;fleet[id]={make,model:name,plate:tag(block,'RegistrationNumber'),fuel:type.fuel,source:owner+' NeTEx via NDOV',sourceId:owner};count++;}sources[source.owner.toUpperCase()]={url:source.url,count};}
fs.writeFileSync(base+'/fleet-prepared.json',JSON.stringify(fleet));
console.log(JSON.stringify({vehicles:Object.keys(fleet).length,sources},null,2));
if(process.argv.includes('--rdw')){
 const plates=[...new Set(Object.values(fleet).map(f=>f.plate.replace(/[^a-zA-Z0-9]/g,'').toUpperCase()).filter(Boolean))];const rdw={};
 for(let i=0;i<plates.length;i+=100){let batch=plates.slice(i,i+100),q=new URLSearchParams({'$select':'kenteken,merk,handelsbenaming','$where':"kenteken in ("+batch.map(p=>"'"+p+"'").join(',')+")",'$limit':'100'});let response=await fetch('https://opendata.rdw.nl/resource/m9d7-ebf2.json?'+q,{headers:{'User-Agent':'SlimOV/0.1.0 build-time public fleet lookup'}});if(!response.ok)throw Error('RDW HTTP '+response.status);for(let row of await response.json())rdw[row.kenteken]=row;}
 for(let f of Object.values(fleet)){let r=rdw[f.plate.replace(/[^a-zA-Z0-9]/g,'').toUpperCase()];if(r){f.make=r.merk||f.make;f.rdwModel=r.handelsbenaming||'';f.source+=' + RDW';}}
 console.log('RDW matches: '+Object.keys(rdw).length);fs.writeFileSync(base+'/rdw-cache.json',JSON.stringify(rdw));
}
fs.writeFileSync('assets/fleet.js','// Public operator vehicle registers, retrieved 2026-11-12. No personal data.\nwindow.FLEET='+JSON.stringify(fleet)+';\nwindow.FLEET_SOURCES='+JSON.stringify(sources)+';');
