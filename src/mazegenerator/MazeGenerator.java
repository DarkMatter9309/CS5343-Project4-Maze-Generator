package mazegenerator;

import java.util.*;

//MazeGenerator class to generate a maze based on the number of rows and columns
public class MazeGenerator {
	// initMaze method to initialize blocks of the maze to be connected later
	private List<List<Block>> initMaze(int r, int c, List<List<Block>> m) {
		int n = 0;
		int p = 0;
		while (p < r) {
			List<Block> row = new ArrayList<>();
			int q = 0;
			while (q < c) {
				// Stores current index in block b
				Block b = new Block(n++, true, true);
				row.add(b);
				q++;
			}
			m.add(row);
			p++;
		}
		return m;
	}

	// pMaze method to print our maze
	private void pMaze(int r, int c, List<List<Block>> m) {
		// Three spaces on top left corner
		System.out.print("   ");
		// top line after the spaces
		int q = 1;
		while(q<c) {
			System.out.print(" __");
			q++;
		}
		// printline
		System.out.println();
		int p = 0;
		while(p<r) {
			if (p == 0) {
				// Leave space on top left corner
				System.out.print(" ");
			} else {
				// left line
				System.out.print("|");
			}
			q = 0;
			while(q < c) {
				if (m.get(p).get(q).h) {
					// Horizontal maze wall
					System.out.print("__");
				} else {
					// broken
					System.out.print("  ");
				}

				if (m.get(p).get(q).v) {
					// Vertical maze wall
					System.out.print("|");
				} else {
					// broken
					System.out.print(" ");
				}
				q++;
			}
			System.out.println();
			p++;
		}
	}

	// cMaze to create maze using disjoint sets
	public void cMaze(int r, int c) {
		//total number of blocks
		int nBlocks = r * c;
		DisjointSets disjointSet = new DisjointSets(nBlocks);

		List<List<Block>> m = new ArrayList<List<Block>>();

		// initialize maze as m with r rows and c columns
		m = initMaze(r, c, m);

		Random rdm = new Random();
		while (disjointSet.find(0) != disjointSet.find(nBlocks - 1)) {
			// cRow = current row as a random number within the range of number of rows
			int cRow = rdm.nextInt(r);
			// cCol = current column as a random number within the range of number of columns
			int cCol = rdm.nextInt(c);
			// get current block from the maze
			Block cBlock = m.get(cRow).get(cCol);
			int cValue = cBlock.vl;
			
			// Find on current block
			int root1 = disjointSet.find(cValue);
			int root2;
			boolean rmHorizontal = false;

			if (cValue == nBlocks - 1) {
				// just continue if bottom-rightmost cell is the curr cell
				continue; 
			}

			if (cRow == r - 1) {
				// cRow is last row > only break the right wall
				//cannot break the wall to the bottom
				root2 = disjointSet.find(cValue + 1);
			} else if (cCol == c - 1) {
				// cCol is last column > only break the bottom wall
				// cannot break the right wall
				root2 = disjointSet.find(cValue + c);
				rmHorizontal = true;
			} else {
				// not last row and not last column
				// Randomly select right wall or bottom wall to break
				boolean selectRight;
				selectRight = rdm.nextBoolean();

				if (selectRight) {
					root2 = disjointSet.find(cValue + 1);
				} else {
					root2 = disjointSet.find(cValue + c);
					rmHorizontal = true;
				}
			}

			// Results
			if (root1 != root2) {
				//Two sets union
				disjointSet.union(root1, root2); 
				if (rmHorizontal) {
					//break horizontal wall
					cBlock.h = false;
				} else {
					// break vertical wall
					cBlock.v = false; 
				}
			}
		}

		//open bottom right corner
		Block destination = m.get(r - 1).get(c - 1);
		destination.h = false;
		destination.v = false;

		// print the maze after creation
		pMaze(r, c, m);
	}

	// Block class to store each block of the maze
	private class Block {
		int vl;
		boolean h;
		boolean v;

		Block(int vl, boolean h, boolean v) {
			this.vl = vl;
			this.h = h;
			this.v = v;
		}
	}

	public static void main(String[] args) {
		//initiailze limit to 20 as per project requirements
		int limit = 20;
		// take input
		Scanner sc = new Scanner(System.in);

		// Take a valid input for the number of rows
		System.out.println("Enter the number of rows you want for the maze:");
		int r = sc.nextInt();
		while (r > limit) {
			System.out.println("Thats too much. Enter a number under 20:");
			r = sc.nextInt();
		}

		System.out.println("Enter the number of rows you want for the maze:");
		int c = sc.nextInt();
		while (c > limit) {
			System.out.println("Thats too much. Enter a number under 20:");
			c = sc.nextInt();
		}
		
		// printing our maze
		System.out.printf("Printing out a maze with %d rows and %d columns:", r, c);
		// newline
		System.out.println();
		// initialize m as maze
		MazeGenerator m = new MazeGenerator();
		// create a maze with r rows and c columns
		m.cMaze(r, c);
		sc.close();
	}

}
