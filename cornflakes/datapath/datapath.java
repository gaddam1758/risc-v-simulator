package datapath;
import assembler.primary_memory;

import java.util.*;
import java.io.Serializable;


public class datapath implements Serializable
{

	//knobs and stats
	Map <Integer,branchobject> branchbuffer= new HashMap<>();

	public int no_of_stalls_data_hazards,no_of_stalls_ctrl_hazards,no_of_cycles,no_of_instructions,
	no_of_data_hazards,no_of_ctrl_hazards,no_of_branch_mispredictions,no_of_ctrl_instructions
	,no_of_alu_instructions,no_of_data_transfer_instructions,no_of_stalls;

	double cpi;
	public int cur_pc,prev_pc,branch_pc,stalled_branch_pc,jalr_pc;

	public boolean pipelined,data_forwarding,disable_writing_to_registers,disable_writing_to_pipelined_regs,watch_pipline_reg;
	public boolean stall_decode;
	int stalls;// this is nothing related with number of stalls

	public datapath()
	{
		no_of_stalls=0;
		no_of_cycles=0;
		no_of_branch_mispredictions=0;
		no_of_data_hazards=0;
		no_of_ctrl_hazards=0;
		no_of_instructions=0;
		no_of_alu_instructions=0;
		no_of_ctrl_instructions=0;
		no_of_stalls_ctrl_hazards=0;
		no_of_stalls_data_hazards=0;
		no_of_data_transfer_instructions=0;
		cpi=0;
		pipelined=false;
		data_forwarding=true;
		disable_writing_to_registers=false;
		disable_writing_to_pipelined_regs=false;
		watch_pipline_reg=false;
		stall_decode=false;
		stalls=0;
		//branch_pc=new Stack<Integer>();
	}

	 boolean check(String inp)
	{
		String temp_comp="00000000000000000000000000000000";
		if( !inp.equals(temp_comp) ) return true;//correct condition
		return false;// not correct instruction
	}


	public  boolean fetch(primary_memory mem,instructions[] instr_que)
	{


		System.out.println("fetch stage  5:" +mem.register[5]);
		int empty_instr=0;

		//condition for piplined
		for(instructions i: instr_que)
		{
			if(i==null) empty_instr++;
		}
		//  RISCV HARDWARE LEVEL FETCH START
		mem.irt = mem.loadwordstr(mem.pc);
		cur_pc = mem.pc;
		mem.pc = mem.pc + 4;
		//	RISCV HARDWARE LEVEL FETCH END
		System.out.println("current pc at fetch:"+ cur_pc);
		/*
					implement number of control hazard, and number of control,alu,data transfer
		*/


		if(check(mem.irt) )//checks if the instrucition is null
		{
			if(empty_instr==4 && !pipelined)
			{
				instr_que[0]=null;mem.pc=mem.pc-4;return true;
			}
			instr_que[0]=new instructions();

			if(pipelined && branchbuffer.containsKey(cur_pc))
			{
				System.out.println("control hazrd at pc: "+cur_pc);
				branchobject cur_branch = branchbuffer.get(cur_pc);
				if(cur_branch.prediction == 1)
				{
					System.out.println("branch taken at fect"+cur_branch.two_bit+","+cur_branch.prediction);
					mem.pc = cur_branch.targetaddress;
				}
				else
				{
					System.out.println("branch not taken at fect");
				}

 			}
			return false;
		}
		else instr_que[0]=null;
		return false;
	}
	public  void detect_hazard(instructions[] instr_que)
	{
		int rd1=-100,rd2=-200,rs1=-300,rs2=-400;
		if(instr_que[2]!=null)
		{
			rd1=instr_que[2].rd;
		}


		if(instr_que[3]!=null)
		{
			rd2=instr_que[3].rd;
		}
		if(instr_que[1]!=null)
		{
			rs1=instr_que[1].rs1;
			rs2=instr_que[1].rs2;
		}
		if(rd1==0) rd1=-100;
		if(rd2==0) rd2=-200;
		//System.out.println("rs1 : "+rs1+",rs2"+rs2+",rd1 :"+rd1+",rd2"+rd2);
		{
			if(rs1==rd1)
				{
					instr_que[1].hazard_type_rs1=1;
					if(instr_que[2].mem_switch==3) instr_que[1].hazard_type_rs1=3;
				}
			else if(rs1==rd2) instr_que[1].hazard_type_rs1=2;
			else instr_que[1].hazard_type_rs1=0;
		}
		//switch(rs2)
		{
			if(rs2==rd1)
				{
					instr_que[1].hazard_type_rs2=1;
					if(instr_que[2].mem_switch==3) instr_que[1].hazard_type_rs2=3;
				}
			else if(rs2==rd2) instr_que[1].hazard_type_rs2=2;
			else instr_que[1].hazard_type_rs2=0;
		}

		// if(instr_que[1].hazard_type_rs2!=0 && instr_que[1].hazard_type_rs2!=3)

		// if(instr_que[1].hazard_type_rs1!=0 && instr_que[1].hazard_type_rs1!=3)
		// 	no_of_data_hazards++;



		return;
	}


	public  void decode(primary_memory mem , instructions[] instr_que)
	{

		if(instr_que[1]==null ) return;
		//if(stall_decode) {stall_decode=false;return;}
		System.out.println("at decode");


		instructions obj=new instructions(mem);
		instr_que[1]=obj;
		//////////////////////////////////////////////////////
		if(obj.id==50 && pipelined)
		{
			jalr_pc=prev_pc;
		}
		// int flagger;
		// if(stall_decode) flagger=1;
		// else flagger =0;
		// System.out.println("at decode :"+obj.type+":"+prev_pc+ ":"+flagger);

			if(obj.id==51 && pipelined)
			{
				branch_pc=prev_pc;
				branchobject new_branch = new branchobject();
				if(branchbuffer.get(prev_pc)==null)
				{
					//int temp1_pc=temp_pc-4;
					System.out.println("new entry made on branch table with pc: "+prev_pc);
					new_branch.branchaddress=prev_pc;
					new_branch.targetaddress=prev_pc + obj.iv * 2 ;
					new_branch.prediction=1;
					new_branch.two_bit=3;
					new_branch.is_there=false;
					branchbuffer.put(prev_pc,new_branch);
					mem.pc=new_branch.targetaddress;
					instr_que[0]=null;
				}
					////////////////////////////////////////

					mem.register[obj.rd]=prev_pc+4;
					System.out.println(obj.rd+","+prev_pc);
					//mem.rxt=prev_pc+4;
					return;

			}
			/////////////////////////////////////////////////////////////////

		if(obj.type==4 && pipelined && !stall_decode)
		{
			branch_pc=prev_pc;////
			//stall_decode=false;
			branchobject new_branch = new branchobject();
			if(branchbuffer.get(prev_pc)==null)
			{
				//int temp1_pc=temp_pc-4;
				System.out.println("new entry made on branch table with pc: "+prev_pc);
				new_branch.branchaddress=prev_pc;
				new_branch.targetaddress=prev_pc+2*instr_que[1].iv;
				new_branch.prediction=0;
				new_branch.two_bit=0;
				new_branch.is_there=false;
				branchbuffer.put(prev_pc,new_branch);
			}
		}
		stall_decode=false;
		if(data_forwarding)detect_hazard(instr_que);
		// handling the nop hazards below
		if(obj.hazard_type_rs1==3 || obj.hazard_type_rs2==3)
		{
			stalls=1;
			System.out.println("stalled as data hazard");
			no_of_stalls++;
			no_of_stalls_data_hazards++;
		}
		// handled the nop hazards
		// 	RISCV HARDWARE LEVEL DECODE START
		if (obj.rs1 >= 0 && obj.rs1 < 32)
                mem.rat = mem.register[obj.rs1];// data forwarding implement here
		if (obj.rs2 >= 0 && obj.rs2 < 32)
							mem.rbt = mem.register[obj.rs2];//data forwarding implenent here
		mem.ivt = obj.iv;// here also

		// RISCV HARDWARE LEVEL DECODE STAR

		if(obj.type == 4 && pipelined)
		{
			if(obj.hazard_type_rs2==0 && obj.hazard_type_rs1==0)//shifted execute to decode for control instructio
				{
					System.out.println("enetered decode for controool hazard");
					//int ra = obj.rs1,rb = obj.rs2;
					alu.executesb(obj.id, mem.rat, mem.rbt, mem.ivt); // SB-type
					//mem.rxt = alu.output;/////////////////////////////////////////////////////////////////////////////////////
					if (alu.output != branchbuffer.get(prev_pc).prediction)
					{
						no_of_branch_mispredictions++;
						System.out.println("wrong prediction for control hazrad");

						if(alu.output == 1)
						{
							mem.pc = mem.ivt * 2 + branch_pc;
							branchbuffer.get(branch_pc).updateprediction(1);
						}
						else
						{
							mem.pc = branch_pc+4;//firts it was temp_pc
							branchbuffer.get(branch_pc).updateprediction(0);
						}
						System.out.println("new pc: "+mem.pc);
						instr_que[0]=null;//bypass
					}
					instr_que[1].bypass=true;// here im flushing this branch instruction next time it shouldnt decode the next instruction as well

				}
			else
				{
					stalled_branch_pc=branch_pc;
				}
		}
	}

	public  void execute(primary_memory mem , instructions[] instr_que)
	{

		if(instr_que[2]==null || instr_que[2].bypass) return ;
		System.out.println("at execute");

		mem.rmt = mem.rb;

		int ra,rb;
		instructions obj=instr_que[2];

		switch(obj.hazard_type_rs1)
		{
			case 1: ra=mem.rx;no_of_data_hazards++;break;
			case 2: ra=mem.ry;no_of_data_hazards++;break;
			case 3: ra=mem.ry;no_of_data_hazards++;break;
			default: ra=mem.ra;
		}
		switch(obj.hazard_type_rs2)
		{
			case 1: rb=mem.rx;no_of_data_hazards++;break;
			case 2: rb=mem.ry;no_of_data_hazards++;break;
			case 3: rb=mem.ry;no_of_data_hazards++;break;
			default: rb=mem.rb;
		}
		if(!data_forwarding)
		{
			ra=mem.ra;
			rb=mem.rb;
		}
		System.out.println(obj.instruct+"  :ra :"+ra+", rb :"+rb+"type rs1"+obj.hazard_type_rs1+"ry:"+mem.ry);


		;// data forwarding exectute here

		// RISCV HARDWARE LEVEL ALU START

		switch (obj.type)
		{


		case 1:
			alu.executer(obj.id, ra, rb); // R-type
			mem.rxt = alu.output;
			break;

		case 2:
			alu.executei(obj.id, ra, mem.iv); // I-type
			int k = alu.output;
			if (obj.id == 50) // jalr
			{
				if(pipelined)
				{
					mem.rxt=jalr_pc+4;
					mem.pc=k;
					instr_que[0]=null;
					instr_que[1]=null;
				}
				else
				{
					mem.rxt = mem.pc;
					mem.pc = k;
				}
			}
			else
			{
				mem.rxt = k;
			}

			break;

		case 3:
			alu.executeS(obj.id, ra, mem.iv); // S-type
			mem.rxt = alu.output;
			break;

		case 5:
			System.out.println(mem.pc+"pc");
			if(!pipelined)
			{
				alu.executeu(obj.id, mem.pc, mem.iv);// U-type
			}
			else
			{
				alu.executeu(obj.id, mem.pc-8, mem.iv);
			}
			mem.rxt = alu.output;
			break;

		case 6:
		alu.executeuj(obj.id, ra, mem.iv);
			if(!pipelined)
			{				 // UJ-type
				mem.rxt = mem.pc;
				mem.pc = mem.pc + mem.iv * 2 - 4;
			}

			break;

		case 4:

		////////////////////////////////////////////////////////////////////////
		if(pipelined)//shifted execute to decode for control instructio
		{
			System.out.println("enetered decode for controool hazard");
			//int ra = obj.rs1,rb = obj.rs2;
			alu.executesb(obj.id, ra, rb, mem.iv); // SB-type
			branchobject check=branchbuffer.get(stalled_branch_pc);
			//mem.rxt = alu.output;/////////////////////////////////////////////////////////////////////////////////////
			if (alu.output != check.prediction)
			{
				no_of_branch_mispredictions++;
				System.out.println("wrong prediction for control hazrad checked in execute");

				if(alu.output == 1)
				{
					mem.pc = mem.iv * 2 + stalled_branch_pc;////
					check.updateprediction(1);
				}
				else
				{
					mem.pc = stalled_branch_pc+4;
					check.updateprediction(0);
				}
				System.out.println("new pc: "+mem.pc);
				instr_que[0]=null;
				instr_que[1]=null;
			}
			instr_que[2].bypass=true;// here im flushing this branch instruction next time it shouldnt decode the next instruction as well
			break;
		}
		////////////////////////////////////////////////////////////////////////

		else
		{
			alu.executesb(obj.id, ra, rb, mem.iv); // SB-type
			mem.rxt = alu.output;
			if (mem.rxt == 1) {
				mem.pc = mem.iv * 2 + mem.pc - 4;
			}
			break;
		}

		}

		// RISCV HARDWARE LEVEL ALU END
	}

	public  boolean memory(primary_memory mem , instructions[] instr_que)

	{
		System.out.println("at memory");

		boolean change_ir=true;
		instructions obj = instr_que[3];
		if(instr_que[4]!=null)
		{
			no_of_instructions++;
			// switch(instr_que[4].type)
			// {
			// 	case 4: no_of_ctrl_instructions++;no_of_ctrl_hazards++;break;
			// 	case 6: no_of_ctrl_instructions++;
			// 	case 2:
			// 				switch(instr_que[4].id)
			// 				{
			// 					case 50: no_of_ctrl_instructions++;
			// 				}
			// }
			switch(instr_que[4].mem_switch)
			{
				case 1:no_of_alu_instructions++;break;
				case 2:no_of_data_transfer_instructions++;break;
				case 3:no_of_data_transfer_instructions++;break;
				case 4:no_of_ctrl_instructions++;break;
				case 5:no_of_ctrl_instructions++;break;
				default:break;
			}
		}
		{
			instr_que[4]=instr_que[3];
			instr_que[3]=instr_que[2];
			if(stalls==0)
			{
				instr_que[2]=instr_que[1];
				instr_que[1]=instr_que[0];
			}
			else
			{
				stalls--;
				change_ir=false;//refer toggle function
				stall_decode=true;
				instr_que[2]=null;

				mem.pc=mem.pc-4;
			}
			instr_que[0]=null;
		}


		int empty_instr=0;
		for(instructions i: instr_que)
		{
			if(i==null) empty_instr++;
		}
		if(obj==null && empty_instr==5)///return for data dependency as there are 5 empty even there
		{
			//if(pipelined && check(mem.loadwordstr(mem.pc))) return false;///changed for bypass
			//if(pipelined) return false;
			toggle(mem,change_ir);
		  return true;
		}
		if(obj==null)
		{
			toggle(mem,change_ir);
 		 	return false;
		}
		switch (obj.mem_switch)
		{
			case 1: // write to reg without memeory access;
			{
				mem.ryt = mem.rx;

				break;
			}
			case 2: // write to memory
			{
				switch (obj.id)
				{

				case 24:
					mem.storeword(mem.rx, mem.rm);
					break;// change for half and byte;

				case 25:
					mem.storehalf(mem.rx, mem.rm);
					break;

				case 26:
					mem.storeword(mem.rx, mem.rm);
					break;

				}

				break;
			}
			case 3:// read from memory
			{
				switch (obj.id) {

				case 1:
					mem.ryt = mem.loadbyte(mem.rx);
					break;

				case 2:
					mem.ryt = mem.loadhalf(mem.rx);
					break;

				case 3:
					mem.ryt = mem.loadword(mem.rx);
					break;

				}

				break;
			}
			case 4:
			{
				mem.ryt = mem.rx;
				break;
			}
			case 5:
			{
				mem.ryt = mem.rx;
				break;
			}

		}

		// HARDWARE LEVEL MEMORY END
		toggle(mem,change_ir);

		return false;
	}
	public  void toggle(primary_memory mem,boolean change_ir)
	{
		if(!disable_writing_to_pipelined_regs)
		{
			mem.ra=mem.rat;
			mem.rb=mem.rbt;
			mem.iv=mem.ivt;
			mem.rx=mem.rxt;
			mem.ry=mem.ryt;
			mem.rm=mem.rmt;

		}
		if(change_ir)
		mem.ir=mem.irt;
			// if(stalls==0)
			//
			// else stalls--;
	}

	public  void write(primary_memory mem , instructions[] instr_que)
	{
		print_que(instr_que);
		System.out.println("at write");



		instructions obj=instr_que[4];

		if(obj==null || obj.bypass) return;
		if(obj.id==51 && pipelined) return;

		//System.out.println("Writing");

		//System.out.println(mem.ry+">");
		//instructions obj = instr_que[4];
		// HARDWARE LEVEL WRITE START
		if(!disable_writing_to_registers)
		{
			if (obj.mem_switch == 1 || obj.mem_switch == 3 || obj.mem_switch == 5)
				{


					if(obj.rd>=0 && obj.rd<32)
					mem.register[obj.rd] = mem.ry;
				}
		}
		if(!disable_writing_to_registers)
		{
				if (obj.mem_switch == 4 && obj.id == 51 )
			{
				if(obj.rd>=0 && obj.rd<32)
				mem.register[obj.rd] = mem.ry;
			}
		}
		// HARDWARE LEVEL WRITE END
		mem.register[0] = 0;
		return ;
	}

        
	public  void run(primary_memory mem,boolean pipelined,
		boolean data_forwarding,
		boolean disable_writing_to_registers,
		boolean disable_writing_to_pipelined_regs,
		boolean watch_pipline_reg,
		boolean stall_decode)
	{
		cur_pc=0;
		mem.pc=0;
		boolean flag;
		instructions[] instr_que=new instructions[5];
		for(instructions i:instr_que)i=null;
		pipelined=pipelined;
                this.data_forwarding=data_forwarding;
                this.disable_writing_to_registers=disable_writing_to_registers;
                this.disable_writing_to_pipelined_regs=disable_writing_to_pipelined_regs;
                this.watch_pipline_reg=watch_pipline_reg;
                this.stall_decode=stall_decode;
		//int n=30;
		while(true)
		{
			//n--;
			
			print_reg(mem);
 		 prev_pc=cur_pc;
		 flag=fetch (mem,instr_que);
		 write  (mem,instr_que);
		 print_reg(mem);
		 decode (mem,instr_que);
		 execute(mem,instr_que);
		 no_of_cycles++;

		 //print_que(instr_que);
		 if(memory (mem,instr_que) && !flag)break;

		}
		calculate_data();
		print_summary();
	}
	public void  print_reg(primary_memory mem)
	{
		System.out.println("ra :"+mem.ra+", rb:"+mem.rb+", rz :"+mem.rx+", ry "+mem.ry+", rm :"+mem.rm +" ");
	}
	public void  calculate_data()
	{
		cpi=(double)no_of_cycles/(double)no_of_instructions;
	}
	public void print_que(instructions[] instr_que)
	{
		System.out.println("<<<<<<<<<<<<");
		for(int i=0;i<5;i++)
		{
			System.out.print(i+" :");
			if(instr_que[i]!=null)
			System.out.print(instr_que[i].instruct);
			System.out.println();
		}
		System.out.println("<<<<<<<<<<<<");
	}
	public void print_summary()
	{
		System.out.println("\nno_of_stalls  :"+no_of_stalls+"\nno_of_cycles  :"+no_of_cycles+
		"\nno_of_branch_mispredictions  :"+no_of_branch_mispredictions+"\nno_of_data_hazards:  "+no_of_data_hazards+
		"\nno_of_ctrl_hazards:  "+no_of_ctrl_hazards+"\nno_of_instructions  :"+no_of_instructions+
		"\nno_of_alu_instructions  :"+no_of_alu_instructions+"\nno_of_ctrl_instructions:  "+no_of_ctrl_instructions+
		"\nno_of_stalls_ctrl_hazards  :"+no_of_stalls_ctrl_hazards+"\nno_of_stalls_data_hazards  :"+no_of_stalls_data_hazards+
		"\nno_of_data_transfer_instructions  :"+no_of_data_transfer_instructions+"\ncpi  :"+cpi);
	}

}
