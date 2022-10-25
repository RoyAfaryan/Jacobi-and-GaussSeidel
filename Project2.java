//Roy Afaryan
//CS3010
//Project 1
//October 10, 2022

import java.util.*;
import java.lang.Math;
import java.io.*;

public class Project2{

    public static void main(String[] args){
 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the number of linear equations: ");
        int numberOfEquations = scanner.nextInt();
        int numberOfCoefficients = numberOfEquations + 1;
        double[][] coefficientMatrix = new double[numberOfEquations][numberOfCoefficients];
        double[][] coefficientMatrix1 = new double[numberOfEquations][numberOfCoefficients];

        System.out.println("Would you like to enter a file? (y or n): ");
        String answer = scanner.next();
        if(answer.compareTo("y") == 0){			//file reader
            System.out.println("Please enter file name: ");
            String filename = scanner.next();
            try{
                fileReader(filename, coefficientMatrix);
	      fileReader(filename, coefficientMatrix1);

            } catch (FileNotFoundException e){
                System.err.println("File error(ReadFile:Main): " + e.getMessage());
                System.exit(1);
            }
        }
        else{
    
            System.out.println("Please enter coefficients (including b values): ");

            for(int i = 0; i < numberOfEquations; i++){     		//loop to enter coefficients
                for(int j = 0; j < numberOfCoefficients; j++){
                    coefficientMatrix[i][j] = scanner.nextInt();		//Jacobi
		coefficientMatrix1[i][j] = coefficientMatrix[i][j];	//Gauss Seidel
                }
            }
            
        }
        	double[] b1 = new double[coefficientMatrix.length];
	double[] b = new double[coefficientMatrix.length];		//nested for to create b values from coef matrix
	for(int i = 0; i < coefficientMatrix.length; i++){
		for(int j = coefficientMatrix[0].length - 1; j > 2; j--){
			b[i] = coefficientMatrix[i][j];		//Jacobi
			b1[i] = b[i];				//Gauss Seidel
		}//end for
	}//end for

	System.out.println("Please enter error value: ");		//userInput for error
	double error = scanner.nextDouble();
	System.out.println();

	System.out.println("Please enter starting solutions: ");		//userInput for starting solutions
        	double[] startingSolutions = new double[numberOfEquations];		
	double[] startingSolutions1 = new double[numberOfEquations];	
	for(int i = 0; i < numberOfEquations; i++){
		startingSolutions[i] = scanner.nextDouble();		//Jacobi
		startingSolutions1[i] = startingSolutions[i];		//Gauss Seidel
	}
	
	System.out.println();
	System.out.println("Jacobi: ");
	Jacobi(coefficientMatrix, b, startingSolutions, error);

	System.out.println();
	System.out.println("Gauss Seidel: ");
	GaussSeidel(coefficientMatrix1, b1, startingSolutions1, error);
    
    }

    public static void Jacobi(double[][] matrix, double[] b, double[] x, double error){
	int kmax = 50;
	double diag = 0;
	double sum = 0;
	double minDiag = Math.pow(10,-10);
	double[] temp = new double[x.length];				//array to check if less than error
	double[] y = new double[x.length];				//next iterative solutions for x

	outer: for(int k = 0; k <= kmax; k++){				//count iterations
		y = Arrays.copyOf(x, x.length);
		for(int i = 0; i < matrix.length; i++)			//traverse matrix rows
		{			
			sum = b[i];
			diag = matrix[i][i];

			if(Math.abs(diag) < minDiag){			//checks diagonal size
				System.out.print("Diagonal element too small.");
				break outer;
			} 

			for(int j = 0; j < matrix[0].length - 2; j++)	//traverse matrix columns
				{	
				if(j != i){
					sum = sum - (matrix[i][j]*y[j]);
					}//end if
				}//end for
				x[i] = sum / diag;
			}//end for
			printArray(x);
			System.out.println("Number of iterations: " + k);
			
			for(int z = 0; z < x.length; z++){
				temp[z] = x[z] - y[z];
				if(Math.abs(temp[z]) < error){
					System.out.println("Final iteration: " + k);
					
					break outer;
				}
			}

		}//endfor

	System.out.println("Maximum iterations reached for error: " + error);			
	
    }

    public static void GaussSeidel(double[][] matrix, double[] b, double[] x, double error){
	int kmax = 50;
	double diag = 0;
	double sum = 0;
	double minDiag = Math.pow(10,-10);
	double[] temp = new double[x.length];				//array to check if less than error
	double[] y = new double[x.length];				//next iterative solutions for x

	outer: for(int k = 0; k <= kmax; k++){				//count iterations
		y = Arrays.copyOf(x, x.length);
		for(int i = 0; i < matrix.length; i++)			//traverse matrix rows
		{			
			sum = b[i];
			diag = matrix[i][i];

			if(Math.abs(diag) < minDiag){			//checks diagonal size
				System.out.print("Diagonal element too small.");
				break outer;
			} 
			for(int j = 0; j < i; j++){
				sum = sum - matrix[i][j]*x[j];
			}
			for(int j = i + 1; j < matrix[0].length - 2; j++){
				sum = sum - matrix[i][j]*x[j];
			}
			x[i] = sum / diag;
		}
			printArray(x);
			System.out.println("Number of iterations: " + k);
		
			for(int z = 0; z < x.length; z++){
				temp[z] = x[z] - y[z];
				if(Math.abs(temp[z]) < error){
					System.out.println("Final iteration: " + k);
					
					break outer;
				}
			}

		}//endfor

	System.out.println("Maximum iterations reached for error: " + error);			
	
    }

    public static void printArray(double[] array){
	System.out.print("[ ");
	for(double element: array){
		System.out.print(element + ", ");
	}
	System.out.print("] ");
    }



    public static void printMatrix(double[][] matrix){
        System.out.print("\nCoefficient matrix: \n");
        for(double[]x:matrix){                  
            for(double y:x){
            System.out.print(y+"      ");
            }
            System.out.println();
        }
    }

    public static double[][] fileReader(String filename, double[][] coefficientMatrix) throws FileNotFoundException{
        
        try(Scanner inFile = new Scanner(new File("project2testfile.txt"))){
            while(inFile.hasNextLine()){
                for(int i = 0; i < coefficientMatrix.length; i++){
                    String[] line = inFile.nextLine().trim().split(" ");
                    for(int j = 0; j < line.length; j++){
                        coefficientMatrix[i][j] = Double.parseDouble(line[j]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
        printMatrix(coefficientMatrix);
        return coefficientMatrix;
        
    }

}