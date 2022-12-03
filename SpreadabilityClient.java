import java.util.*;
import java.io.*;
//to compile: javac SpreadabilityClient.java
//to run: java -cp . SpreadabilityClient BCCPhi0x20.spr 1e-6

public class SpreadabilityClient {
	public static double pi = Math.PI;
	public static void main(String[] args) throws IOException {
		double[] finalParams = new double[3];
		double[] paramsN = largeTAnalysis(importData(args[0]), Double.parseDouble(args[1]), 1);
		double[] paramsDS = largeTAnalysis(importData(args[0]), Double.parseDouble(args[1]), 2);
		double[] paramsO = largeTAnalysis(importData(args[0]), Double.parseDouble(args[1]), 3);
		if (paramsN[0] < paramsDS[0]) {
			if (paramsN[0] < paramsO[0]) {
				finalParams = paramsN;
				System.out.println("The system is nonstealthy.");
			}
			else {
				finalParams = paramsO;
				System.out.println("The system is ordered stealthy.");
			}
		}
		else {
			if (paramsDS[0] < paramsO[0]) {
				finalParams = paramsDS;
				System.out.println("The system is disordered stealthy.");
			}
			else {
				finalParams = paramsO;
				System.out.println("The system is ordered stealthy.");
			}
		}
		System.out.println("Final t_s = " + finalParams[0]);
		System.out.println("Final gamma = " + finalParams[1]);
		System.out.println("Final C = " + finalParams[2]);
	}

	/**obtain the set-in time t_l, exponent alpha and coefficient C
 * 	@param spr double[][], spreadability
 * 	@param epsilon double, allowed deviation from the large-time formula
 * 	@return double[3], (t_l, alpha, C)*/
	public static double[] largeTAnalysis(double[][] spr, double epsilon, int stealthyness) {
		/**intial guesses*/
		double tl = 1;
		double expo = 3;
		double c = 0.1;
		double[] sl = new double[spr.length];
		double sigma = 10;/**stoping criterion for expo*/
		while (sigma > 1e-6) {
			double[][] sprForFit = selectionX(spr, tl, spr[spr.length - 1][0]);
			switch (stealthyness) {
				case 1:
					for (int i = 0; i < sprForFit.length; i++) {
						sprForFit[i][0] = Math.log(sprForFit[i][0]);
						sprForFit[i][1] = Math.log(sprForFit[i][1]);
					}
					break;
				case 2:
					for (int i = 0; i < sprForFit.length; i++) {
                                                sprForFit[i][1] = Math.log(sprForFit[i][0]*sprForFit[i][1]);
                                        }
					break;
				case 3:
					for (int i = 0; i < sprForFit.length; i++) {
                                                sprForFit[i][1] = Math.log(sprForFit[i][1]);
                                        }
					break;
			}
			double[] params = linearRegression(sprForFit);
			sigma = Math.abs(expo + params[0]);
			expo = -params[0];
			c = Math.exp(params[1]);
			switch (stealthyness) {
				case 1:
					for (int i = 0; i < sl.length; i++) {
						sl[i] = c*Math.pow(spr[i][0], -expo);
					}
					break;
				case 2:
					for (int i = 0; i < sl.length; i++) {
                                        	sl[i] = c*Math.exp(-expo*spr[i][0])/spr[i][0];
                                	}
					break;
				case 3:
					for (int i = 0; i < sl.length; i++) {
                                                sl[i] = c*Math.exp(-expo*spr[i][0]);
                                        }
					break;
			}	
			tl = getSetInTime(spr, sl, epsilon);
			System.out.println(tl + " " + expo + " " + c);
		}
		double[] result = new double[3];
		result[0] = tl;
		result[1] = expo;
		result[2] = c;
		return result;
	}

	private static double[][] selectionX(double[][] f, double x1, double x2) {
		double[][] temp = new double[f.length][2];
		int count = 0;
		for (int i = 0; i < f.length; i++) {
			if (f[i][0] > x1 && f[i][0] < x2) {
				temp[count][0] = f[i][0];
				temp[count][1] = f[i][1];
				count ++;				
			}
		}
		double[][] result = new double[count][2];
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < 2; j++) {
				result[i][j] = temp[i][j]; 
			}
		}
		return result;		
	}

	/** linear regression that computes (slope, y-intercept)*/
	private static double[] linearRegression(double[][] f) {
		int n = f.length;
		double sx = 0;
		double sy = 0;
		double sxy = 0;
		double sxx = 0;
		for (int i = 0; i < n; i++) {
			double x = f[i][0];
			double y = f[i][1];
			sx += x;
			sy += y;
			sxy += x*y;
			sxx += x*x;
		}
		double xBar = sx/(double)n;
		double yBar = sy/(double)n;
		double m = (n*sxy - sx*sy)/(n*sxx - sx*sx);
		double c = yBar - m*xBar;
		double[] result	= new double[2];
		result[0] = m;
		result[1] = c;
		return result;
	}

	/**computes the set-in time t_l(epsilon)
 * 	@param f double[][], the actual spreadability
 * 	@param fl double[][], the proposed long-time behavior of spreadability
 * 	@param epsilon double, the allowed difference between the two above
 * 	@return double set-in time*/
	private static double getSetInTime(double[][] f, double[] fl, double epsilon) {
		for (int i = f.length - 1; i >= 0; i--) {
			double t = f[i][0];/**time*/
			double difference = Math.abs(f[i][1] - fl[i]);
			if (difference > epsilon) {
				return f[i + 1][0];
			}
		}
		return 0;
	}

	public static double[][] importData(String fileName) throws IOException {
                double[][] tempResult = new double[20000][2];
                Scanner s = null;
                int line = 0; /** this will be the number of lines in the file */
                try {
                        String path = fileName;
                        s = new Scanner(new BufferedReader(new FileReader(path)));
                        while (s.hasNextDouble() && line < 20000) {
                                double x = s.nextDouble();
                                double y = s.nextDouble();
                                tempResult[line][0] = x;
                                tempResult[line][1] = y;
                                line ++;
                        }
                }
                finally {
                        s.close();
                }
                double[][] result = new double[line][2];
                for (int i = 0; i < line; i++) {
                        result[i][0] = tempResult[i][0];
                        result[i][1] = tempResult[i][1];
                }
                return result;
        }
}
