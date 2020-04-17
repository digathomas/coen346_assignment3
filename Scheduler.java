    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.atomic.AtomicInteger;

    public class Scheduler extends Thread {

        Clock clock;
        List<Process> processes = new ArrayList<Process>(); //list of all processes 
        List<Process> running =  new ArrayList<Process>(); //list of processes running
        List<Process> ready = new ArrayList<Process>(); //list of processes in ready state
        List<Process> terminated = new ArrayList<Process>(); //list of processes that have completed their burst time
        AtomicInteger CPUAccess = new AtomicInteger(2); //CPU access keys , limited to two since only 2 processes can run at the same time

        int numberOfProcesses; //number of total processes

        Scheduler(List<Process> processList, List<Process> readyList,List<Process> runningList,List<Process> terminatedList,Clock clock1, int numOfPro, AtomicInteger key) {
            processes = processList;
            ready = readyList;
            running = runningList;
            terminated = terminatedList;
            clock = clock1;
            numberOfProcesses = numOfPro;
            CPUAccess = key;
        }

        public static void move(List<Process> departure, List<Process> destination) { //move the process from departure to destination
            destination.add(departure.get(0)); //get the process from departure list and add it to destination list
            departure.remove(0); //remove the the process after it has been added to the destination list
        }

        public void run() {

           // System.out.println(clock.getTime());
           // System.out.println(numberOfProcesses);

            while (terminated.size() < numberOfProcesses) //if the size of the terminated list is smaller than that of the number of processes
            { 
                Process process1=null;
                int flag = 0;
                int flag2 = 0;
                //while (running.size() < 2)
                // { //while the runningList size is less than 2, it'll search for a new process to run
        
                if(processes.isEmpty()) flag=1;
                while(flag==0 && CPUAccess.get()>0){ //check if there are enough keys to enter the while loop, so it can add a process 
               
                    if(processes.get(0).getArrival()==clock.getTime()) { //if you check the process list and there is a process with proper arrival time
                        
                        processes.get(0).start(); //start it 
                        move(processes, running); //move it to the running list
                        CPUAccess.getAndDecrement(); //decrement the number of keys available
                        if(processes.isEmpty()) flag=1;  //if processes is empty, then flag is set to 1, and while loop condition is no longer satisfied 
                    }
                    else flag=1;
                }

                if(ready.isEmpty()) flag2 =1;
                while (CPUAccess.get() >0 && flag2 ==0) 
                {
                    
                    if(!ready.isEmpty()) { //while the ready list is not empty get a process from there
                       
                        move(ready, running); //move it from the ready to the running list 
                        System.out.println("Clock: "+ clock.getTime()+" Process: " + running.get(0).getProcessID()+ " Resumed");
                        CPUAccess.getAndDecrement(); //get a key and decrement it 
                            
                    }
                    else flag2 = 1;
                    }
                
                    //printing the output 
                    if(!running.isEmpty() && running.get(0).getArrival()==clock.getTime())
                    {
                    System.out.println("Clock: "+ clock.getTime()+" Process: " + running.get(0).getProcessID()+ " Started");
                    }
                    if(!running.isEmpty()) process1=running.remove(0);

                    if(!running.isEmpty()&& running.get(0).getArrival()==clock.getTime())//add arrival condition
                    System.out.println("Clock: "+ clock.getTime()+" Process: " + running.get(0).getProcessID()+ " Started");

                    if(process1!=null)
                        {process1.setLock();
                        process1.setClock(clock.getTime());}

                    if(!running.isEmpty()) 
                        {running.get(0).setLock();
                        running.get(0).setClock(clock.getTime());}
                        
                 //System.out.println(CPUAccess.get());

                    while(CPUAccess.get()<2); //Wait for them to finish:wait for CPUAccess to be 2 again           
                   //System.out.println(CPUAccess.get());

                    
                    clock.increment(); //increment the clock by the time quantum

                         if(process1 !=null)   
                         System.out.println("Clock: "+ clock.getTime()+" Process: " + process1.getProcessID()+ " Paused");

                         if(!running.isEmpty())
                         System.out.println("Clock: "+ clock.getTime()+" Process: " + running.get(0).getProcessID()+ " Paused");
                      
                        if(process1!=null)
                        {
                        if(process1.getBurstTime()==0) //check to see if the burst time is 0
                        {System.out.println("Clock: "+ clock.getTime()+" Process: " + process1.getProcessID()+ " Finished");
                        terminated.add(process1);
                        }
                        else ready.add(process1);
                        }

                        if(!running.isEmpty()) {
                        if(running.get(0).getBurstTime()==0) //check to see if burst time is 0
                        {System.out.println("Clock: "+ clock.getTime() + " Process: " +running.get(0).getProcessID()+ " Finished");
                        move(running,terminated); } //if burst time is 0, then it has terminated and is added to terminated list
                        else move(running,ready); //otherwise it goes back to ready list
                    }
             
                    }

            }
 
        }
    
