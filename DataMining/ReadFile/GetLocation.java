import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GetLocation {
    HashMap<Integer, orderInfo> eachLocation=new HashMap<Integer,orderInfo >();//保存每个聚类的平均经纬度
    /**
     * 地球半径，单位m
     */
    private static final double EARTH_RADIUS = 6378137;

    /**
     * 根据经纬度，计算两点间的距离
     *
     * @param longitude1 第一个点的经度
     * @param latitude1  第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2  第二个点的纬度
     * @return 返回距离，单位m
     */
    public static double getDistance1(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 米
        s =  s * EARTH_RADIUS;
        return s;
    }
    private long readFile(File jwfile,File poifile) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream=new FileInputStream(jwfile);
        Scanner scanner = new Scanner(fileInputStream, StandardCharsets.UTF_8).useDelimiter(",").useDelimiter("\n");

        FileInputStream poifileInputStream=new FileInputStream(poifile);
        Scanner poiscanner = new Scanner(poifileInputStream,"GBK").useDelimiter(",").useDelimiter("\n");

        long counts=0;
        while(scanner.hasNextLine())
        {
            String line=scanner.nextLine();
            if(counts==0)
            {
                counts++;
                continue;
            }
            float jd=Float.parseFloat(line.split(",")[0]);
            float wd=Float.parseFloat(line.split(",")[1]);
            int type=Integer.parseInt(line.split(",")[2]);
            if(eachLocation.get(type)==null)
            {
                orderInfo info=new orderInfo(jd,wd);
                eachLocation.put(type,info);
            }
            else {
                eachLocation.get(type).cnt++;
                eachLocation.get(type).jd+=jd;
                eachLocation.get(type).wd+=wd;
            }
            counts++;
        }

        for (Map.Entry<Integer, orderInfo> entry : eachLocation.entrySet()) {
            eachLocation.get(entry.getKey()).jd/=entry.getValue().cnt;
            eachLocation.get(entry.getKey()).wd/=entry.getValue().cnt;
        }

        File file = new File("D:\\NNU\\exercise\\Java\\DataMining\\Economic\\result.csv");
        FileOutputStream out=new FileOutputStream(file);
        int poicount=0;
        while(poiscanner.hasNextLine())
        {
            String line=poiscanner.nextLine();
            if(poicount==0) {
                poicount++;
                continue;
            }
            String name=line.split(",")[0];
            String type=line.split(",")[1];
            float jd=Float.parseFloat(line.split(",")[3]);
            float wd=Float.parseFloat(line.split(",")[4]);

            for (Map.Entry<Integer, orderInfo> entry : eachLocation.entrySet()) {
                float njd=entry.getValue().jd;
                float nwd=entry.getValue().wd;
                Double dis=getDistance1(jd,wd,njd,nwd);
                if(dis<100.0)
                {
                    eachLocation.get(entry.getKey()).poicnt++;
                    eachLocation.get(entry.getKey()).poi.add(new Store(type,name));//存 店的类别和店名

                    if(eachLocation.get(entry.getKey()).type.get(type)==null)
                    {
                        eachLocation.get(entry.getKey()).type.put(type,1);
                    }
                    else {
                        Integer cnt=eachLocation.get(entry.getKey()).type.get(type);
                        eachLocation.get(entry.getKey()).type.put(type,cnt+1);
                    }
                }
            }
            poicount++;
        }
        for (Map.Entry<Integer, orderInfo> entry : eachLocation.entrySet()) {
            String type="";
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(entry.getValue().type.entrySet());
            list.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if(i==2) break;
                if(type!="") type+="、";
                type+=list.get(i).getKey();
            }
            for(int i=0;i<entry.getValue().poi.size();i++)
            {
                String outs=entry.getKey()+","+entry.getValue().jd+","+entry.getValue().wd+","+
                        entry.getValue().poi.get(i).type+","+entry.getValue().poi.get(i).name+","+entry.getValue().cnt+"\n";
                System.out.println(outs);
                out.write(outs.getBytes("GBK"));
            }
        }
        return counts;
    }
    public static void main(String[] args) throws IOException {
        File jwfile=new File("D:\\NNU\\exercise\\Java\\DataMining\\Economic\\night_DBSCAN.csv");
        File poifile=new File("D:\\NNU\\exercise\\Java\\DataMining\\Economic\\经济POI.csv");
        GetLocation mL=new GetLocation();
        long counts=mL.readFile(jwfile,poifile);
        System.out.println(counts);

    }
}
class orderInfo
{

    public Float jd;

    public Float wd;

    public HashMap<String,Integer> type;

    public Integer cnt;
    public Integer poicnt;
    public Vector<Store> poi;

    orderInfo()
    {
        jd=0.0F;
        wd=0.0F;
        type=new HashMap<String, Integer>();
        cnt=1;
        poicnt=0;
        poi=new Vector<>();
    }
    orderInfo(Float JD,Float WD)
    {
        jd=JD;
        wd=WD;
        type=new HashMap<String,Integer>();
        cnt=1;
        poicnt=0;
        poi=new Vector<>();
    }
}
class Store{
    String type;
    String name;
    Store()
    {
        type="";
        name="";
    }
    Store(String Type,String Name)
    {
        type=Type;
        name=Name;
    }
}