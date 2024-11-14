import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GossipSimulation1
{
    static List<GossipNode> GossipNodes;
    static CountDownLatch begin;
    static CountDownLatch end;
    static int size=1000;
    static int rounds;
    public static void main(String[] args) throws IOException {
        File outfile=new File("src/out/k值-收敛轮数、误差.csv");
        FileOutputStream fos = new FileOutputStream(outfile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        System.out.println("k,rounds,error");
        bw.write("k,rounds,error\n");
        for(int j=0;j<100;j++)
        {
            GossipNodes= new ArrayList<>();
            rounds=0;
            double sum=0;
            for(int i=1;i<size;i++)
            {
                double randomValue = Math.random()*100;
                GossipNode GossipNode = new GossipNode(0,randomValue,1+0.2*j);
                GossipNodes.add(GossipNode);
                sum+=randomValue;
            }
            for(int i=1;i<=1;i++)
            {
                double randomValue = Math.random()*100;
                GossipNode GossipNode = new GossipNode(1,randomValue,1+0.2*j);
                GossipNodes.add(GossipNode);
                sum+=randomValue;
            }
            ExecutorService threadPool = Executors.newFixedThreadPool(size);
            boolean flag=true;
            while (flag)
            {
                flag=false;
                end=new CountDownLatch(size);
                begin=new CountDownLatch(1);
                rounds ++;
                List<Future<Boolean>> futures = new ArrayList<>();
                for(GossipNode GossipNode:GossipNodes)
                {
                    GossipNode.setGossipNodes(GossipNodes);
                    GossipNode.setBegin(begin);
                    GossipNode.setEnd(end);
                    Future<Boolean> result = threadPool.submit(GossipNode);
                    futures.add(result);
                }
                begin.countDown();
                try {
                    end.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    for (Future<Boolean> future : futures) {
                        if (future.get()) {
                            flag = true;
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            double error=0;
            double average = sum/size;
            for(GossipNode GossipNode:GossipNodes)
            {
                error+=Math.abs(GossipNode.value-average);
            }
            System.out.println((1+0.2*j)+","+rounds+","+error/size);
            bw.write((1+0.2*j)+","+rounds+","+error/size+"\n");
        }
        bw.close();
        fos.close();
        System.exit(1);
    }
}
