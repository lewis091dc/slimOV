import fs from 'node:fs';
function csv(s){let rows=[],row=[],v='',q=false;for(let i=0;i<s.length;i++){let c=s[i];if(c==='"'){if(q&&s[i+1]==='"'){v+='"';i++}else q=!q}else if(c===','&&!q){row.push(v);v=''}else if(c==='\n'&&!q){row.push(v.replace(/\r$/,''));rows.push(row);row=[];v=''}else v+=c}if(v){row.push(v);rows.push(row)}let h=rows.shift();return rows.map(r=>Object.fromEntries(h.map((k,i)=>[k,r[i]||''])))}
const base=process.argv[2]||'build/data';
const agencies=Object.fromEntries(csv(fs.readFileSync(base+'/agency.txt','utf8')).map(a=>[a.agency_id,a.agency_name]));
const routes=Object.fromEntries(csv(fs.readFileSync(base+'/routes.txt','utf8')).map(r=>[r.route_id,[r.route_short_name,r.route_long_name,r.agency_id,+r.route_type,r.route_color]]));
const stops=csv(fs.readFileSync(base+'/stops.txt','utf8')).filter(s=>+s.stop_lat>50.7&&+s.stop_lat<53.7&&+s.stop_lon>3.2&&+s.stop_lon<7.3).map(s=>[s.stop_id,s.stop_name,Math.round(+s.stop_lat*1e6)/1e6,Math.round(+s.stop_lon*1e6)/1e6,s.parent_station,s.platform_code,s.stop_code]);
const date=fs.existsSync(base+'/date.txt')?fs.readFileSync(base+'/date.txt','utf8').trim():new Date().toISOString().slice(0,10);
fs.mkdirSync('assets',{recursive:true});fs.writeFileSync('assets/catalog.js','window.CATALOG='+JSON.stringify({date,agencies,routes,stops})+';');console.log(stops.length+' stops; '+Object.keys(routes).length+' routes');
