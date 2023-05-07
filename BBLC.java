
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

class assigned{
	public int v;
	public String name;
	public String arith_1;
	public String arith_2;
	public String arith_3;
	
	

	
	
}

public class BBLC{
	public static void main(String args[]) throws IOException {
		String BBLC_file = args[0];
		
		//checking if an input file is provided and if it is valid

		
		check_input_file(BBLC_file);
		//spliting and trimming the input file into an array of strings

		String [] content_array = getting_content(BBLC_file);
		
		
		
		//checking tejhiz

		
		checktejhiz(content_array);
		//removing the first index in content array so check ra2m can process the array of string correctly 

		String[] content_array_check_ra2em = new String[content_array.length-1];
		System.arraycopy(content_array, 1, content_array_check_ra2em, 0, content_array_check_ra2em.length);
		
		//check_ra2m return an array of assigned which is the registers
		//regester.name holds the name of the register and regester.v holds the value of the register
		assigned [] regesters = check_ra2m(content_array_check_ra2em);
		
		//after we check ra2m we need to get the length in the array of assigned which is the registers used 
		//if we used only 4 register so the other 6 will be null so the length to delete for the 
		// new content array for barmajeh_1 is 4 so that the content_array starts with[#barmajeh]
		
		int index_to_delete =0 ;
		try {
		while(regesters[index_to_delete] !=null) {
			index_to_delete++;
		}
		}catch(java.lang.ArrayIndexOutOfBoundsException e) {
			System.out.println("Error! you can only use 9 registers");
			System.exit(0);
			
		}
		/// here we are catching if more than 9 registers are used 

		
		// fixing the content array for barmajeh 1 as we mentioned before

		String[] content_array_barmajeh_1 = new String[content_array_check_ra2em.length-index_to_delete];
		
		System.arraycopy(content_array_check_ra2em,index_to_delete,content_array_barmajeh_1,0,content_array_barmajeh_1.length);
		
		//this boolean array is used for writing the output file 
		//each index in this boolean are represents an specific output
		//[0,1,2,3,4,5]
		//index 0 represents if it is addition or subtraction
		// index 1 represents if in the arithmetic equation 2 registers are used to add or sub example j=j+i or j=j-i
		//index 2 represents if j=i+num
		// index 3 represents if j=num+i
		// index 4 represents if j=num+num
		//index 5 represents if in the iza clause a register is used to compare example iza i=j
		// all of these indexes are true if the condition is met
		boolean[] output_barmajeh_1 = barmajeh_1(content_array_barmajeh_1,regesters);
		
		//we are removing the first index in the content array which is #barmajeh so barmajeh_2 can run without errors

		String[] content_array_barmajeh_2 = new String[content_array_barmajeh_1.length-1];
		
		System.arraycopy(content_array_barmajeh_1,1,content_array_barmajeh_2,0,content_array_barmajeh_2.length);
		

		
		assigned[] if_and_sub_block = barmajeh_2(content_array_barmajeh_2);
		
		//this if_and sub_block return an array of assigned 
				//iza i=5
				//j=j+i
				//[0,1]
				//0.name = i
				//0.arith_1 = 5
				//1.arith_1 = j
				//1.arith_2 = j
				//1.arith_3 = i
		
		write_output_file(regesters,if_and_sub_block,output_barmajeh_1);
		//the first argument is used to write the registers and creating a hashmap of registers
				//the second argument is used to write  the output file if a regester is used we used the hashmap.get(if_and_sub[index].arith)
				// if a number is used we just print the if_and_sub.arith
		
		System.out.println("the assembly code is wriiten succsefully in Prog.s file ");
		
		
		
		
		
		
		
	}
	
	public static boolean check_input_file(String input_file_path) {
		boolean flag = false;
		// to check if a bbl file is provided as an argument 
		if(input_file_path.length()==0) {
			System.out.println("Error! please provide the complier with a .bbl file");
			System.exit(0);
			
		}
		// to check if the bbl file is not empty
	    File bbl_input_file = new File(input_file_path);
	    if(bbl_input_file.length() == 0 ) {
	    	System.out.println("Error! please provide bbl code to the .bbl file");
			System.exit(0);

	    }
	    else {
	    	flag = true;
	    }
	    
	    return flag;

	}
	
	public static String[] getting_content(String input_file_path) throws IOException {
	    File bbl_input_file = new File(input_file_path);
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(bbl_input_file))) {
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) {
	            stringBuilder.append(line.trim());

	            stringBuilder.append("\n");  
	        }

	        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

	        String content = stringBuilder.toString();

	        String[] content_2 = content.split("\n");
	        
	        for (int i = 0; i < content_2.length; i++) {
	            content_2[i] = content_2[i].trim();
	        }
	        return content_2;
	    }

	}
	public static void checktejhiz(String[] content_arr) {
	    boolean tejhiz_sec = false;

    	if(content_arr[0].startsWith("#tejhiz")) {
	    		tejhiz_sec = true;
	    		
	    }
	    else {
	   		System.out.println("Error! wirte the bbl file corretcly with Variable section titled: \"#tejhiz\"");
	   		System.exit(0);
	    
	    }
	}
	
	public static assigned[] check_ra2m(String[] content_arr) {
	    assigned[] regesters = new assigned[11];
	    int index = 0;
	    
	    for (String s : content_arr) {
	        if (s.startsWith("ra2m")) {
	            // splitting "ra2m i=0" results in ["ra2m", "i=0"]
	            String[] parts = s.split(" ");
	            // splitting "i=0" results in ["i", "=", "0"]
	            String[] subParts = parts[1].split("=");
	            
	            if (subParts.length == 2) {
	                try {
	                    int num_ass = Integer.parseInt(subParts[1]);
	                    regesters[index] = new assigned();
	                    regesters[index].name = subParts[0];
	                    regesters[index].v = num_ass;
	                } catch (NumberFormatException e) {
	                    System.out.println("Error! please assign the variable with an integer");
	                    System.exit(0);
	                }
	            } else {
	                System.out.println("Error! please assign the variable name to an integer using the equals sign \"=\"");
	                System.exit(0);
	            }
	            index++;
	        } else if (s.startsWith("#barmajeh")) {
	            break;
	        } else {
	            System.out.println("Error! to declare a number start with ra2em or to proceed to the barmajeh section start with \"#barmajeh\"");
	            System.exit(0);
	        }
	    }
	    
	    return regesters;
	}
	
	
	
	
	public static boolean[] barmajeh_1(String[] content_arr,assigned[] regester_assign_arr) {
		boolean[] checking_for_output = new boolean[6] ;
		
		
		boolean barmajeh = false;
		if(content_arr[0].startsWith("#barmajeh")) 
		{
			barmajeh = true;
		}
		
		else 
		{
			System.out.println("Error! to start barmajeh section start with \"#barmajeh\" ");
			barmajeh = false;
			System.exit(0);
		}
		
		if(barmajeh) 
		{
			assigned[] if_caluse = new assigned[1];
			String [] content_arr_2 = new String[content_arr.length-1];
			System.arraycopy(content_arr, 1, content_arr_2, 0, content_arr_2.length);

			if(content_arr_2[0].startsWith("iza")) 
			{
				barmajeh = true;
			}
			else 
			{
				System.out.println("Error! in the barmajeh section to start an if clause use \"iza\"");
				System.exit(0);
			}
			
			
			//splitting iza i=5:
			String[] parts = content_arr_2[0].split(" ");
			
			//splitting the parts[1] i=5: into sub parts:
			
			String[] subParts = parts[1].split("");
			
			if(subParts[1].startsWith("=") && subParts[3].startsWith(":")) 
			{
				try 
				{
				
		    		
		    		
		    		//creating a hashmap to see if the variable that is being compared is in the register
		    		
		    		HashMap<String,Integer> hash_regis = new HashMap<>();
		    		for(int i = 0 ;i<regester_assign_arr.length && regester_assign_arr[i] != null;i++) 
		    		{
		    			hash_regis.put(regester_assign_arr[i].name, regester_assign_arr[i].v);
		    		}
		    		
		    		boolean flag_to_continue = false;
			    	if_caluse[0] = new assigned();
			    	
			    	// iza =5: or iza=k: we are storing 5 or k in .arith_1 which is string to see if this key is in the hashmap of registers


			    	if_caluse[0].arith_1 = subParts[2];

			    	// to see if in the iza clause it comapred to another register or a integer

			    	
			    	if(hash_regis.containsKey(if_caluse[0].arith_1)) {
		    			checking_for_output[5] = true;

		    		}
			    	else {
		    			checking_for_output[5] = false;

			    	}
			    	//if it is a register 

			    	if(checking_for_output[5] == true) {
			    		
			    		if_caluse[0].name = subParts[0];
			    		
			    		if (hash_regis.containsKey(if_caluse[0].name)) {
			    			flag_to_continue = true;
			    			
			    		}
			    		else {
			    			System.out.println("Error! compare the variable with  an integer or another variable having them both assigned in the tejhiz section ");
			    			System.exit(0);
			    			
			    		}
			    		
			    		
			    		
			    	}
			    	// if it is an integer

			    	if(checking_for_output[5] == false) {
		    		int num_com = Integer.valueOf(subParts[2]);
			    	//if_caluse[0] = new assigned();
			    	if_caluse[0].name = subParts[0];
		    		if_caluse[0].v = num_com;
		    		//if_caluse[0].arith_1 = subParts[2];
		    		
		    		
			    	
		    		
		    		if (hash_regis.containsKey(if_caluse[0].name)) {
		    			flag_to_continue = true;
		    			
		    		}
		    		else {
		    			System.out.println("Error! compare the variable with  an integer or another variable having them both assigned in the tejhiz section ");
		    			System.exit(0);
		    			
		    		}
			    	}
		    		if(flag_to_continue) 
		    		{
		    			String[] sub_block = content_arr_2[1].split("");
		    			
		    			
		    			if(hash_regis.containsKey(sub_block[0]) && sub_block[1].startsWith("=")) 
		    			{
		    				flag_to_continue = true;
		    			}
		    			else 
		    			{
		    				System.out.println("Error! in the sub block of the if clause save the Arithmetic equation using an \"=\" to a variable that was assigned in the tejhiz section");
		    				System.exit(0);
		    			}
		    			
		    			if(flag_to_continue) 
		    			{
		    				if(sub_block[3].startsWith("+")) 
		    				{
	    						checking_for_output[0] = true;

		    					// if j=j+i
		    					if(hash_regis.containsKey(sub_block[2]) && hash_regis.containsKey(sub_block[4])) 
		    					{
		    						checking_for_output[1] = true;
		    					}
		    					else 
		    					{
		    						try 
		    						{
		    							// if j=j+num
		    							if(hash_regis.containsKey(sub_block[2])) 
		    							{
		    								try {
			    								int num_to_sub = Integer.valueOf(sub_block[4]);
			    								checking_for_output[2] = true;
			    								}catch(NumberFormatException e) {
			    									System.out.println();
			    									System.out.printf("in the sub block of the iza statement %s is not an integer nor a variable assigned in the tejhiz phase",sub_block[2]);
			    									System.out.println();
			    									System.exit(0);
		    							}
		    							}
		    							// if j = num+i
		    							else if (hash_regis.containsKey(sub_block[4])) 
		    							{
		    								try {
			    								int num_to_sub = Integer.valueOf(sub_block[2]);
			    								checking_for_output[3] = true;
			    								}catch(NumberFormatException e) {
			    									System.out.println();
			    									System.out.printf("in the sub block of the iza statement %s is not an integer nor a variable assigned in the tejhiz phase",sub_block[2]);
			    									System.out.println();
			    									System.exit(0);
		    							}
		    							}
		    							// if j=num+num 
		    							else if (!hash_regis.containsKey(sub_block[2]) && !hash_regis.containsKey(sub_block[4])) 
		    							{
		    								try {
			    								int num_to_sub = Integer.valueOf(sub_block[4]);
			    								int num_to_sub_2 = Integer.valueOf(sub_block[2]);
			    								checking_for_output[4] = true;
			    								}catch(NumberFormatException e) {
			    									System.out.println();
			    									System.out.printf("in the sub block of the iza statement %s is not an integer nor a variable assigned in the tejhiz phase",sub_block[2]);
			    									System.out.println();
			    									System.exit(0);

		    							}
		    							
		    						}
		    						}catch(NumberFormatException e) 
		    						{
		    							System.out.println("Error! use assigned variables in the teghiz section or integers in the arithmetyic operations ");

		    						}
		    					}
		    				}
		    				
		    				else if (sub_block[3].startsWith("-")) 
		    				{
	    						checking_for_output[0] = false;

		    					// if j=j-i
		    					if(hash_regis.containsKey(sub_block[2]) && hash_regis.containsKey(sub_block[4])) 
		    					{
		    						checking_for_output[1] = true;
		    					}
		    					else 
		    					{
		    						try 
		    						{
		    							// if j=j-num
		    							if(hash_regis.containsKey(sub_block[2])) 
		    							{
		    								try {
		    								int num_to_sub = Integer.valueOf(sub_block[4]);
		    								checking_for_output[2] = true;
		    								}catch(NumberFormatException e) {
		    									System.out.println();
		    									System.out.printf("in the sub block of the iza statement %s is not an integer nor a variable assigned in the tejhiz phase",sub_block[2]);
		    									System.out.println();
		    									System.exit(0);
		    								}
		    							}
		    							// if j = num-i
		    							else if (hash_regis.containsKey(sub_block[4])) 
		    							{
		    								try {
			    								int num_to_sub = Integer.valueOf(sub_block[2]);
			    								checking_for_output[3] = true;
			    								}catch(NumberFormatException e) {
			    									System.out.println();
			    									System.out.printf("in the sub block of the iza statement %s is not an integer nor a variable assigned in the tejhiz phase",sub_block[2]);
			    									System.out.println();
			    									System.exit(0);
			    								}
		    							}
		    							// if j=num-num 
		    							else if (!hash_regis.containsKey(sub_block[2]) && !hash_regis.containsKey(sub_block[4])) 
		    							{
		    								try {
			    								int num_to_sub = Integer.valueOf(sub_block[2]);
			    								int num_to_sub_2 = Integer.valueOf(sub_block[4]);
			    								checking_for_output[4] = true;
			    								}catch(NumberFormatException e) {
			    									System.out.println();
			    									System.out.printf("in the sub block of the iza statement %s is not an integer nor a variable assigned in the tejhiz phase",sub_block[2]);
			    									System.out.println();
			    									System.exit(0);
			    								}

		    							}
		    							
		    						}catch(NumberFormatException e) 
		    						{
		    							System.out.println("Error! use assigned variables in the teghiz section or integers in the arithmetic operations ");

		    						}
		    					}
		    				}
		    				else 
		    				{
		    					System.out.println("Error! you can use only arithmetic operation \"+\" or \"-\" ");
		    					System.exit(0);
		    				
		    				}
		    				
		    			}
		    			
		    			
		    			
		    			
		    			
		    			
		    		}
		    		
		    		
		    		
		    		
		    		
		    		
					
				}catch(NumberFormatException e) 
				{
	    			System.out.println("Error! please compare the variable assigned in the tejhiz section with another variable assigned in tejhiz or with an integer ");
	    			System.exit(0);
				}
				
			}
			
			else 
			{
				System.out.println("Error ! please compare the variable name to a integer using the equals sign \"=\" and end your iza statement with \":\" ");
	    		System.exit(0);
			}
			
			
			
			
		}
	
	return checking_for_output;
	
	}

	
	
	public static assigned[] barmajeh_2(String[] content_arr) {
		assigned[] if_and_sub_block = new assigned[2];
		//content_arr = [iza i=5:,j=j+i]
		
		//splitting iza i=5:
		String[] part_1 = content_arr[0].split(" ");
		
		//splitting i=5:
		String[] subParts_1 = part_1[1].split("");
		if_and_sub_block[0] = new assigned();

		if_and_sub_block[0].name = subParts_1[0];
		if_and_sub_block[0].arith_1 = subParts_1[2];
		
		//splitting j=j+i
		String[] part_2 = content_arr[1].split("");
		if_and_sub_block[1] = new assigned();
		if_and_sub_block[1].arith_1 = part_2[0];
		if_and_sub_block[1].arith_2 = part_2[2];
		if_and_sub_block[1].arith_3 = part_2[4];

		return if_and_sub_block;
		
	}
	
	public static File write_output_file(assigned[] regesters,assigned[] if_and_sub,boolean[] checking_for_out) throws IOException {
		File output_file = new File("Prog.s");
	    BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));
	    for (int i = 0; i < regesters.length && regesters[i] != null; i++) {
	        String str = String.format("MOV R%d,#%d", i, regesters[i].v);
	        writer.write(str);
	        writer.newLine();  
	    }		
		
		HashMap<String,Integer> hash_regis = new HashMap<>();
		for(int i = 0 ;i<regesters.length && regesters[i] != null;i++) 
		{
			hash_regis.put(regesters[i].name, regesters[i].v);
		}
		
		
		
		
		HashMap<String,Integer> Regester_index = new HashMap<>();

		for(int i = 0;regesters[i]!=null;i++) {
			
			Regester_index.put(regesters[i].name, i);
		}
	    writer.newLine();
	    
	    if(checking_for_out[5] == true) {
		    String str_1 = String.format("CMP  R%d,R%d",Regester_index.get(if_and_sub[0].name),Regester_index.get(if_and_sub[0].arith_1));
		    writer.write(str_1);
		    writer.newLine();
	    }
	    else if(checking_for_out[5] == false) {
		    String str_2 = String.format("CMP  R%d,#%s",Regester_index.get(if_and_sub[0].name),if_and_sub[0].arith_1);
		    writer.write(str_2);
		    writer.newLine();

	    }
	    
	    String str_3 = "BEQ  if_block:";
	    writer.write(str_3);
	    writer.newLine();
	    String str_4 = "if_block:";
	    writer.write(str_4);

	    writer.newLine();
	    
	    if(checking_for_out[0] == true) {
	    	if(checking_for_out[1] == true) {
	    		String str_5 = String.format("ADD R%d,R%d,R%d", Regester_index.get(if_and_sub[1].arith_1),Regester_index.get(if_and_sub[1].arith_2),Regester_index.get(if_and_sub[1].arith_3));
	    		writer.write(str_5);
	    	}
	    	else if(checking_for_out[2] ==true) {
	    		String str_6 = String.format("ADD R%d,R%d,#%s", Regester_index.get(if_and_sub[1].arith_1),Regester_index.get(if_and_sub[1].arith_2),if_and_sub[1].arith_3);
	    		writer.write(str_6);

	    	}
	    	
	    	else if(checking_for_out[3] ==true) {
	    		String str_7 = String.format("ADD R%d,#%s,R%d", Regester_index.get(if_and_sub[1].arith_1),if_and_sub[1].arith_2,Regester_index.get(if_and_sub[1].arith_3));
	    		writer.write(str_7);
	    		
	    	}
	    	
	    	else if(checking_for_out[4] ==true){
	    		String str_8 = String.format("ADD R%d,#%s,#%s", Regester_index.get(if_and_sub[1].arith_1),if_and_sub[1].arith_2,if_and_sub[1].arith_3);
	    		writer.write(str_8);
	    		
	    	}
	    	
	    }
	    else if(checking_for_out[0] ==false) {
	    	if(checking_for_out[1] == true) {
	    		String str_5 = String.format("SUB R%d,R%d,R%d", Regester_index.get(if_and_sub[1].arith_1),Regester_index.get(if_and_sub[1].arith_2),Regester_index.get(if_and_sub[1].arith_3));
	    		writer.write(str_5);
	    	}
	    	else if(checking_for_out[2] ==true) {
	    		String str_6 = String.format("SUB R%d,R%d,#%s", Regester_index.get(if_and_sub[1].arith_1),Regester_index.get(if_and_sub[1].arith_2),if_and_sub[1].arith_3);
	    		writer.write(str_6);

	    	}
	    	
	    	else if(checking_for_out[3] ==true) {
	    		String str_7 = String.format("SUB R%d,#%s,R%d", Regester_index.get(if_and_sub[1].arith_1),if_and_sub[1].arith_2,Regester_index.get(if_and_sub[1].arith_3));
	    		writer.write(str_7);
	    		
	    	}
	    	
	    	else if(checking_for_out[4] ==true){
	    		String str_8 = String.format("SUB R%d,#%s,#%s", Regester_index.get(if_and_sub[1].arith_1),if_and_sub[1].arith_2,if_and_sub[1].arith_3);
	    		writer.write(str_8);
	    		
	    	}
	    	
	    }
	    

	    
	    writer.close();
	    return output_file;
	}
	
	
	
}