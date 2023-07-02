import java.io.*;
import java.util.*;

public class Read {
    HashMap<String, Info> map=new HashMap<String, Info>();
    HashMap<Integer,HashMap<String, Info> > eachmap=new HashMap<Integer,HashMap<String, Info>>();
    private long readFile(File file) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream=new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(",").useDelimiter("\n");
        long counts=0;

        HashMap<String, Boolean> ordermap = new HashMap<String, Boolean>();
        HashMap<String, Boolean> drivermap = new HashMap<String, Boolean>();
        PriorityQueue<Integer> bigHeap=new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        while(scanner.hasNextLine())
        {
            String line=scanner.nextLine();
            String driverID=line.split(",")[0];
            String orderID=line.split(",")[1];
            int time=Integer.parseInt(line.split(",")[2]);
            float jd=Float.parseFloat(line.split(",")[3]);
            float wd=Float.parseFloat(line.split(",")[4]);
            drivermap.put(driverID,true);
            ordermap.put(orderID,true);

            if(map.get(orderID)==null)
            {
                map.put(orderID, new Info());
            }
            int start=map.get(orderID).start;
            int end=map.get(orderID).end;

            if(start>time) {
                map.get(orderID).start=time;
                map.get(orderID).sjd=jd;
                map.get(orderID).swd=wd;
            }
            if(end<time) {
                map.get(orderID).end=time;
                map.get(orderID).ejd=jd;
                map.get(orderID).ewd=wd;
            }
            counts++;
        }
        System.out.println(drivermap.size());
        System.out.println(ordermap.size());
        int sT=1478275200;
        for (Map.Entry<String, Info> entry : map.entrySet()) {
//            System.out.println("orderID="+entry.getKey()+" startTime="+entry.getValue().start+" endTime="+entry.getValue().end+
//                    " jd="+entry.getValue().jd+" wd="+entry.getValue().wd);
            int hours=(entry.getValue().start-sT)/3600;
            if(eachmap.get(hours)==null)
            {
                HashMap<String, Info> nmap=new HashMap<String, Info>();
                eachmap.put(hours,nmap);
            }
            eachmap.get(hours).put(entry.getKey(),entry.getValue());
        }
        return counts;
    }
    public void writeFile() throws IOException {
        File file = new File("D:\\NNU\\exercise\\Java\\DataMining\\Economic\\night.csv");
        FileOutputStream out=new FileOutputStream(file);
        if (!file.exists()) {
            file.createNewFile();
        }
        String column="orderID,startTime,endTime,sjd,swd,ejd,ewd\n";
        out.write(column.getBytes());
        for(int i=18;i<=26;i++)
        {
            int count=i%24;
            for (Map.Entry<String, Info> entry : eachmap.get(count).entrySet()) {
//            System.out.println("orderID="+entry.getKey()+" startTime="+entry.getValue().start+" endTime="+entry.getValue().end+
//                    " jd="+entry.getValue().jd+" wd="+entry.getValue().wd);
                String outs=entry.getKey()+","+entry.getValue().start+","+entry.getValue().end
                        +","+entry.getValue().sjd+","+entry.getValue().swd+","+entry.getValue().ejd+","+entry.getValue().ewd+"\n";
                out.write(outs.getBytes());
            }
        }
//        for (Map.Entry<String, Info> entry : map.entrySet()) {
////            System.out.println("orderID="+entry.getKey()+" startTime="+entry.getValue().start+" endTime="+entry.getValue().end+
////                    " jd="+entry.getValue().jd+" wd="+entry.getValue().wd);
//            String outs=entry.getKey()+","+entry.getValue().start+","+entry.getValue().end
//                    +","+entry.getValue().sjd+","+entry.getValue().swd+","+entry.getValue().ejd+","+entry.getValue().ewd+"\n";
//            out.write(outs.getBytes());
//        }
        out.flush();
        out.close();
        System.out.println("Done");
    }
    public void write24File() throws IOException {
        int count=0;
        while(count<24)
        {
            File file = new File("D:\\NNU\\exercise\\Java\\DataMining\\Data\\data"+count+".csv");
            FileOutputStream out=new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            String column="orderID,startTime,endTime,jd,wd\n";
            out.write(column.getBytes());
            for (Map.Entry<String, Info> entry : eachmap.get(count).entrySet()) {
//            System.out.println("orderID="+entry.getKey()+" startTime="+entry.getValue().start+" endTime="+entry.getValue().end+
//                    " jd="+entry.getValue().jd+" wd="+entry.getValue().wd);
                String outs=entry.getKey()+","+entry.getValue().start+","+entry.getValue().end
                        +","+entry.getValue().sjd+","+entry.getValue().swd+","+entry.getValue().ejd+","+entry.getValue().ewd+"\n";
                out.write(outs.getBytes());
            }
            out.flush();
            out.close();
            System.out.println("Done"+count);
            count++;
        }
    }
    public static void main(String[] args) throws IOException {
        File file=new File("D:\\NNU\\exercise\\Java\\DataMining\\gps_20161105.csv");
        Read mread=new Read();
        long counts=mread.readFile(file);
        System.out.println(counts);
        mread.writeFile();
//        mread.write24File();
    }
}
class Info
{
    public Integer start;
    public Integer end;

    public Float sjd;

    public Float swd;

    public Float ejd;

    public Float ewd;
    Info()
    {
        start=Integer.MAX_VALUE;
        end=0;
        sjd=0.0F;
        swd=0.0F;
        ejd=0.0F;
        ewd=0.0F;
    }
}