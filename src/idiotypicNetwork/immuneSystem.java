/**
 * 
 */
package idiotypicNetwork;

import java.util.List;




/**
 * @author Mario
 *
 */
public class immuneSystem {

	protected int antigens;
	protected int bcells;
	protected int tcells;
	protected int antigensPresentigCells;
	protected int antibodies;
	protected String[] possibleStates = {"Immunity","Tolerance"};
	public String state = "";
	
	
	public immuneSystem(int antigens, int bcells, int tcells, int antigensPresentigCells, int antibodies) {
		super();
		this.antigens = antigens;
		this.bcells = bcells;
		this.tcells = tcells;
		this.antigensPresentigCells = antigensPresentigCells;
		this.antibodies = antibodies;
	} 
	
	
	
	
	
	
	
	
}
