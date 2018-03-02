import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Solver 
{
	public static char[][] fillTheBoard(char[][] board, int size, int[] positions)
	{
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				if(j == positions[i])
				{
					board[j][i] = '*';
				}
				else
				{
					board[j][i] = '_';
				}
			}
		}
		char[][] filledBoard = new char[size][size];
		filledBoard = board;
		return filledBoard;
	}
	
	public static char[][] solve(char[][] board, int size, int[] positions)
	{
		List<Integer> cols = new ArrayList<Integer>();
		List<Integer> rows = new ArrayList<Integer>();
		Random r = new Random();
		int colToMove = 0;
		int oldRow = 0;
		int newRow = 0;
		int conf;
		int maxConf;
		for(int iter = 0; iter < size * 2; iter++)
		{
			maxConf = 0;
			for(int i = 0; i < size; i++)
			{
				conf = countConflicts(positions[i], i, board, size, positions);
				if(conf == maxConf)
				{
					cols.add(i);
					rows.add(positions[i]);
				}
				else if(conf > maxConf)
				{
					maxConf = conf;
                    cols.clear();
                    rows.clear();
                    cols.add(i);
                    rows.add(positions[i]);
				}
			}
			if(maxConf == 0)
			{
				break;
			}
			int num = r.nextInt(cols.size());
			colToMove = cols.get(num);
			oldRow = rows.get(num);
			int minConf = size;
			rows.clear();
			for (int i = 0; i < size; i++) 
			{
				conf = countConflicts(i, colToMove, board, size, positions);
                if (conf == minConf) 
                {
                    rows.add(i);
                } 
                else if (conf < minConf) 
                {
                    minConf = conf;
                    rows.clear();
                    rows.add(i);
                }
            }
			if (!rows.isEmpty()) 
			{
				newRow = rows.get(r.nextInt(rows.size()));
            }
			positions[colToMove] = newRow;
			//System.out.println("NewRow: " + newRow);
			if(newRow != oldRow)
			{
				board[newRow][colToMove] = '*';
				board[oldRow][colToMove] = '_';
			}
		}
		return board;
	}
	
	public static int countConflicts(int row, int col, char[][] board,
			int size, int[] positions) 
	{
		int count = 0;
		for(int i = 0; i < size; i++)
		{
			if (i == col)
			{
				continue;
			}
			int r = positions[i];
			if(r == row || Math.abs(r-row) == Math.abs(i-col))
			{
				count++;
			}
		}
		return count;
	}
	
	public static void writeToFile(String filename, char[][] board, int size) 
	{
	    try 
	    {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
	        for(int i = 0; i < size; i++) 
	        {
	            for(int j = 0; j < board[i].length; j++) 
	            {
	                if(j == board[i].length - 1)
	                {    
	                    bw.write(board[i][j]);
	                } 
	                else
	                {
	                    bw.write(board[i][j] + " ");
	                }        
	            }
	            bw.newLine();
	        }
	        bw.flush();
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}

	public static void main(String[] args) 
	{
		int size = 0;
		Scanner s = new Scanner(System.in);
		while(size < 4)
		{
			System.out.println("Enter a number between 4 and 10000: ");
			size = s.nextInt();
		}
		char[][] emptyBoard = new char[size][size];
		int[] positions = new int[size];
		Random r = new Random();
		char[][] filledBoard = new char[size][size];
		int conflicts = 0;
		char[][] finalBoard = new char[size][size];
		do
		{
			conflicts = 0;
			for(int i = 0; i < size; i++)
			{
				positions[i] = r.nextInt(size);
				//System.out.print(positions[i] + " ");
			}
			filledBoard = fillTheBoard(emptyBoard, size, positions);
			finalBoard = solve(filledBoard, size, positions);
			for(int i = 0; i < size; i++)
			{
				conflicts += countConflicts(positions[i], i, finalBoard, size, positions);
			}
		}
		while(conflicts != 0);
		writeToFile("F:/Java/workspace/Queens/src/output.txt", finalBoard, size);
		s.close();
	}
}
