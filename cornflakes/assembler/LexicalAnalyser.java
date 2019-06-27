package assembler;

/*
 * this class takes one instruction line as input to
 * constructor and returns tokens and if label is found it returns label
 * in label string and is label is set to true
*/


import java.util.ArrayList;

public class LexicalAnalyser
	{

		boolean ifDelimiter(char c)
			{
				if (c == ' ' || c == '\t' || c == '(' || c == ')' || c == ','||c==':')
					return true;
				return false;
			}

		public ArrayList<String> Tokens = new ArrayList<String>();
		public String label;
		public boolean islabel = false;
		StringBuilder word = new StringBuilder();
           

		// ArrayList<Integer> tokens = new ArrayList<>(Integer);
	public LexicalAnalyser(String line,boolean assemblydirective)
			{
                                
				boolean foundcomment = false;
				boolean isDelimitter = false;
				StringBuilder str = new StringBuilder();
				char temp;
				if(line.length() > 0)
				{
					temp = line.charAt(0);
				}
				int i = 0;
				while  (i< line.length())
					{
					temp = line.charAt(i);
					    if (temp == '#')
						   foundcomment = true;
					    else if (temp == ':'&&!assemblydirective)
					    {
							islabel = true;
							if (Tokens.size() != 0)
							{
								System.out.println("error");
							}

					    }
							 
						else if (ifDelimiter(temp))
							{
								isDelimitter = true;
							
					
							}
							

						if (foundcomment)
							return;
						if (isDelimitter)
							{
                                while(i<line.length() && ifDelimiter(line.charAt(i))){
								 temp = line.charAt(i);
								i++;
							}
								isDelimitter = false;
								Tokens.add(str.toString());
								str.delete(0, str.length());
						        i=i-1;
                                                                
							} 
						else if (islabel&&!assemblydirective)
							
						{
								label = str.toString();
								str.delete(0, str.length());
								islabel = true;
							
						} 
						else
							str.append(temp);
                                  i++;
							 
						
					}
				Tokens.add(str.toString()); 
				str.delete(0, str.length());
			}

		
		 /* public static void main(String args[]) {
		  
		  LexicalAnalyser l=new LexicalAnalyser("add    X1,  X2, "
		  		+ " X3 ",false);
		  for(int i=0;i<l.Tokens.size();i++)
		  System.out.println(l.Tokens.get(i));
		  
		  
		  }
		 */
	}
