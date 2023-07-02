import java.awt.*;
import java.io.*;
import java.util.*;

public class DealTxt {
    Vector<Point> vector=new Vector<>();
    private long readFile(int count) throws FileNotFoundException, IOException {
        File file = new File("D:\\NNU\\exercise\\Java\\DataMining\\MapData\\"+count+"\\"+count+".txt");
        FileInputStream fileInputStream=new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream).useDelimiter("\n");
        long counts=0;
        while(scanner.hasNextLine())
        {
            String line=scanner.nextLine();
            if(line.length()==0) continue;
            if(line.charAt(0)!='('){
                continue;
            }
            int index=line.indexOf(')');
            line=line.substring(index+1,line.length());
            String str="";
            for(int i=0;i<line.length();i++)
            {
                if(line.charAt(i)=='>')
                {
                    str+=",";
                    continue;
                }
                if(line.charAt(i)!=' ' && line.charAt(i)!='-')
                {
                    str+=line.charAt(i);
                }

            }
            String strlist[]=str.split(",");
            if(strlist[2].equals("NOISE")==false)
            {
                vector.add(new Point(Float.parseFloat(strlist[0]),Float.parseFloat(strlist[1]),Integer.parseInt(strlist[2])));
            }
            counts++;
        }
        return counts;
    }

    public void write24File(int count) throws IOException {
        File file = new File("D:\\NNU\\exercise\\Java\\DataMining\\MapData\\"+count+"\\"+count+".csv");
        FileOutputStream out=new FileOutputStream(file);
        if (!file.exists()) {
            file.createNewFile();
        }
        String column="jd,wd,type\n";
        out.write(column.getBytes());
        for(int i=0;i<vector.size();i++)
        {
            String s=vector.get(i).jd+","+vector.get(i).wd+","+vector.get(i).type+"\n";
            out.write(s.getBytes());
        }
        out.flush();
        out.close();
    }
    public static void main(String[] args) throws IOException {
        DealTxt dealTxt=new DealTxt();
        int index=99;
        long counts=dealTxt.readFile(index);
        System.out.println(counts);
        dealTxt.write24File(index);
    }
}
class Point
{
    public Float jd;
    public Float wd;
    public Integer type;
    Point()
    {
        jd=0.0F;
        wd=0.0F;
        type=-1;
    }
    Point(Float Jd,Float Wd,Integer Type)
    {
        jd=Jd;
        wd=Wd;
        type=Type;
    }
}