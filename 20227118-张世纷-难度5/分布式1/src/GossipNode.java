import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class GossipNode implements Callable<Boolean> {
    double value;
    int status; //0-未感染 1-已感染 2-隔离
    double k; //兴趣损失
    double interest; //传播兴趣
    double isolateValue; //interest小于该值时，被隔离
    double minDifference;  //当value差值小于minDifference时，认为两节点value相等
    List<GossipNode> GossipNodes;
    CountDownLatch begin;
    CountDownLatch end;

    public GossipNode(int status,double value,double k)
    {
        this.status=status;
        this.value=value;
        this.k = k;
        this.interest=1.0;
        this.isolateValue=0.01;
        this.minDifference=0.001;
    }

    public void setGossipNodes(List<GossipNode> GossipNodes)
    {
        this.GossipNodes=GossipNodes;
    }

    public CountDownLatch getBegin() {
        return begin;
    }

    public void setBegin(CountDownLatch begin) {
        this.begin = begin;
    }

    public CountDownLatch getEnd() {
        return end;
    }

    public void setEnd(CountDownLatch end) {
        this.end = end;
    }

    public Boolean call() {
        try {
            begin.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int self = GossipNodes.indexOf(this);

        if(this.status==1 && this.interest>=Math.random()) {
            int random = (int)(Math.random()*GossipNodes.size());
            while (random == self)
            {
                random=(int)(Math.random()*GossipNodes.size());
            }
            GossipNode GossipNode = GossipNodes.get(random);
            if (self < random) {
                synchronized (GossipNode) {
                    synchronized (this) {
                        //当前节点可传播
                        if (GossipNode.status != 2 && Math.abs(GossipNode.value - this.value) > GossipNode.minDifference) {
                            double average = (GossipNode.value + this.value) / 2;
                            GossipNode.value = average;
                            GossipNode.status = 1;
                            this.value = average;
                            end.countDown();
//                            System.out.println(self + "感染了" + random);
                            return true;
                        }
                        //当前节点已隔离
                        else if (GossipNode.status == 2 || (GossipNode.status != 2 && Math.abs(GossipNode.value - this.value) <= this.minDifference)) {
                            this.interest = this.interest / this.k;
                            //兴趣值小于等于临界值，GossipNode变为已隔离状态
//                            System.out.println(self+"兴趣值下降");
                            if (this.interest <= this.isolateValue) {
                                this.status = 2;
//                                System.out.println(self+"被隔离");
                            }
                            end.countDown();
                            return false;
                        }
                    }
                }
            } else {
                synchronized (this) {
                    synchronized (GossipNode) {
                        //当前节点可传播
                        if (GossipNode.status != 2 && Math.abs(GossipNode.value - this.value) > GossipNode.minDifference) {
                            double average = (GossipNode.value + this.value) / 2;
                            GossipNode.value = average;
                            GossipNode.status = 1;
                            this.value = average;
                            end.countDown();
//                            System.out.println(self + "感染了" + random);
                            return true;
                        }
                        //当前节点已隔离
                        else if (GossipNode.status == 2 || (GossipNode.status != 2 && Math.abs(GossipNode.value - this.value) <= this.minDifference)) {
                            this.interest = this.interest / this.k;
                            //兴趣值小于等于临界值，GossipNode变为已隔离状态
//                            System.out.println(self+"兴趣值下降");
                            if (this.interest <= this.isolateValue) {
                                this.status = 2;
//                                System.out.println(self+"被隔离");
                            }
                            end.countDown();
                            return false;
                        }
                    }
                }
            }
        }
        end.countDown();
        return false;
    }
}

