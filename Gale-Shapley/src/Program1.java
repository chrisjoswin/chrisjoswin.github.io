/*
 * Name: Christopher Erattuparambil
 * EID: ce7347
 */

import java.util.*;
/*
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
	/*
	 * Determines whether a candidate Matching represents a solution to the
	 * Stable Marriage problem. Study the description of a Matching in the
	 * project documentation to help you with this.
	 */



	public boolean isStableMatching(Matching allocation) {
		/*TODO implement this function */

		ArrayList<ArrayList<Integer>> serverTable= new ArrayList<>();//servertable pref
		ArrayList<ArrayList<Integer>> userTable= new ArrayList<>();//user pref
		ArrayList<Integer> pairs= new ArrayList<>();//matching
		serverTable=allocation.getServerPreference();
		userTable=allocation.getUserPreference();
		pairs=allocation.getUserMatching();
		int slotCount=allocation.totalServerSlots();
		int numJobs=allocation.getUserCount();
		int  allowedNeg;
		allowedNeg=numJobs-slotCount;
		int size=pairs.size();


		int countNeg=0;
		for(int k=0;k<pairs.size();k++)
		{
			if(allocation.getUserMatching().get(k)==-1)
			{
				countNeg++;
			}

		}
		if(allowedNeg<countNeg||countNeg==pairs.size())
		{

			return false;
		}
		//
		for(int i=0;i<size-1;i++)
		{
			for(int j= 0; j<size;j++)
			{
				if(i!=j)
				{
					int user1=i;
					int user2 =j;
					int server1=pairs.get(i);
					int server2 =pairs.get(j);

					boolean b=true;
					boolean user1switch =false;
					boolean server2switch = false;
					boolean user2switch = false;
					boolean server1switch = false;
					ArrayList<Integer> user1Pref= allocation.getUserPreference().get(user1);
					ArrayList<Integer>user2Pref =allocation.getUserPreference().get(user2);
					ArrayList<Integer>serverSlotCount=allocation.getServerSlots();


					if(server2==-1&&server1==-1)
					{

					}
					else if(server2==-1)
					{

						ArrayList<Integer>server1Pref = allocation.getServerPreference().get(server1);	
						int user2idx=server1Pref.indexOf(user2);
						int user1idx =server1Pref.indexOf(user1);
						if(user2idx<user1idx)
						{
							return false;
						}
					}
					else if(server1==-1)
					{					
						ArrayList<Integer> server2Pref = allocation.getServerPreference().get(server2);
						int user2idx=server2Pref.indexOf(user2);
						int user1idx =server2Pref.indexOf(user1);
						if(user1idx<user2idx)
						{
							return false;
						}
					}
					else 
					{
						//both have servers assigned

						ArrayList<Integer>server1Pref = allocation.getServerPreference().get(server1);
						ArrayList<Integer>server2Pref = allocation.getServerPreference().get(server2);

						//checking if user1 wants server2 and if server2 wants user1
						int server1idx=user1Pref.indexOf(server1);
						int server2idx= user1Pref.indexOf(server2);
						if(server2idx<server1idx)
						{
							user1switch= true;
						}

						int user1idx=server2Pref.indexOf(user1);
						int user2idx= server2Pref.indexOf(user2);
						if(user1idx<user2idx)
						{
							server2switch= true;
						}


						//checking if user2 wants server1 and if server1 wants user2
						server1idx=user2Pref.indexOf(server1);
						server2idx= user2Pref.indexOf(server2);
						if(server1idx<server2idx)
						{
							user2switch= true;
						}


						user1idx=server1Pref.indexOf(user1);
						user2idx= server1Pref.indexOf(user2);
						if(user2idx<user1idx)
						{
							server1switch= true;
						}

						//Checking if there is an instability
						if(user1switch&&server2switch)
						{

							return false; 
						}

						if(user2switch&&server1switch)
						{

							return false;


						}
					}
				}
			}
		}
		return true;
	}

	/*
	 * Determines a solution to the Stable Marriage problem from the given input
	 * set. Study the project description to understand the variables which
	 * represent the input to your solution.
	 * 
	 * @return A stable Matching.
	 */
	public Matching stableMarriageGaleShapley(Matching allocation) {
		int numSlots=0;
		int totalSlots=allocation.totalServerSlots();
		ArrayList<Integer>slotCount =new ArrayList<Integer>();
		for( int k=0;k<allocation.getServerSlots().size();k++)
		{
			slotCount.add(allocation.getServerSlots().get(k));
		}
		int finalMatching[] = new int[allocation.getUserCount()];
		ArrayList<ArrayList<Integer>> serverTable= new ArrayList<ArrayList<Integer>>();

		for( int k=0;k<allocation.getServerPreference().size();k++)
		{
			ArrayList<Integer> temp =new ArrayList<Integer>();
			for(int l=0;l<allocation.getServerPreference().get(k).size();l++)
			{
				temp.add(allocation.getServerPreference().get(k).get(l));
			}
			serverTable.add(temp);
		}
		ArrayList<ArrayList<Integer>> userTable= new ArrayList<ArrayList<Integer>>();
		for( int k=0;k<allocation.getUserPreference().size();k++)
		{
			ArrayList<Integer> temp =new ArrayList<Integer>();
			for(int l=0;l<allocation.getUserPreference().get(k).size();l++)
			{
				temp.add(allocation.getUserPreference().get(k).get(l));
			}
			userTable.add(temp);
		}

		for(int i=0;i<allocation.getUserCount();i++)//setting the (user,server) to -1
		{
			finalMatching[i]=-1;
		}
		while(numSlots<totalSlots)
		{
			for(int askServer =0;askServer<allocation.getServerCount();askServer++)
			{ 	
				int increment =0;
				while(slotCount.get(askServer)>0)
				{	
					int potUser=serverTable.get(askServer).get(increment);//get askserver top pref
					if(finalMatching[potUser]==-1)
					{
						finalMatching[potUser]= askServer;
						numSlots++;
						slotCount.set(askServer,(slotCount.get(askServer)-1));
						increment++;
					}
					else//if user is paired already
					{
						int oldServer= finalMatching[potUser];
						//callSwitch(potUser,oldServer,askServer, userTable);

						ArrayList<Integer>userPrefList=userTable.get(potUser);
						int oldServeridx= userPrefList.indexOf(oldServer);
						int askServeridx= userPrefList.indexOf(askServer);
						if(askServeridx<oldServeridx)//if user prefers asking server
						{
							finalMatching[potUser]=askServer;
							serverTable.get(oldServer).remove(0);
							slotCount.set(askServer,(slotCount.get(askServer)-1));
							slotCount.set(oldServer,(slotCount.get(oldServer)+1));

						}

						else// if user prefers old server
						{
							serverTable.get(askServer).remove(0);
						}
					}

				}
			}
		}
		ArrayList<Integer> ending= new ArrayList<>();//converting arry to arrayList
		for(int i=0;i<finalMatching.length;i++)
		{
			ending.add(finalMatching[i]);
		}
		allocation.setUserMatching(ending);
		return allocation;
	}
}





