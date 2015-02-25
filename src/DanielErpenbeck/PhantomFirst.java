package DanielErpenbeck;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import edu.stanford.rsl.conrad.data.numeric.InterpolationOperators;
import edu.stanford.rsl.tutorial.parallel.ParallelProjector2D;
import ij.ImageJ;

public class PhantomFirst extends Grid2D{

	public PhantomFirst(int width, int height)
	{
		super(width, height);

		//putPixelValue(this.getWidth()/2, this.getHeight()/2, 1.0);

		//DrawRectangle(width/4, height/4, height/4, 1.0);
		
		DrawCircle(width/4, height/4, 40, 0.8);
		DrawCircle(width * 3/4, height/4, 40, 0.8);
		DrawCircle(width/2, height/2, 150, 1.0);
		DrawCircle(width/2, height * 2/3, 55, 0.6);
		DrawCircle(width * 2/5, height * 2/5, 20, 0.15);
		DrawCircle(width * 3/5, height * 2/5, 20, 0.15);
	}
	
	public PhantomFirst(int width, int height, int bla)
	{
		super(width, height);

		DrawRectangle(width/4 * 3, height/4 * 3, height/4, 1.0);
	}

	public void DrawCircle(int x, int y, int radius, double intensity)
	{
		for(int i = 0; i < this.getWidth(); ++i)
		{
			for(int j = 0; j < this.getHeight(); j++)
			{ 
				if((i-x)*(i-x) + (j-y)*(j-y) < (radius * radius))
				{
					putPixelValue(i,j,intensity);
				}
			}
		}
	}
	
	public void DrawRectangle(int x, int y, int size, double intensity)
	{
		for(int i = 0; i < size; ++i)
		{
			for(int j = 0; j < size; ++j)
			{
				putPixelValue(x+i,j+y,intensity);
			}
		}
	}

	public Grid2D CreateSinogram()
	{
		Grid2D sinogram = new Grid2D(this.getWidth(), 180);

		for(int s = -this.getWidth()/2; s < this.getWidth()/2; ++s)
		{
			for(int theta = 0; theta < 180; ++theta)
			{
				double degree = theta * Math.PI/180;
				float sum = 0;

				if(theta < 45 || theta > 135)
				{
					for(int y = 0; y < this.getHeight(); ++y)
					{
						double x = (s - (y - this.getHeight()/2) * Math.sin(degree))/Math.cos(degree) + this.getWidth()/2;

						float interpolate = InterpolationOperators.interpolateLinear(this, x, y);

						sum += interpolate;
					}					
				}

				else
				{
					for(int x = 0; x < this.getWidth(); ++x)
					{
						double y = (s - (x - this.getWidth()/2) * Math.cos(degree))/ Math.sin(degree) + this.getHeight()/2;
						
						float interpolate = InterpolationOperators.interpolateLinear(this, x, y);
						
						sum += interpolate;
					}

				}
				sinogram.putPixelValue(s + this.getWidth()/2, theta, sum);
			}
		}

		return sinogram;

	}
	

	public static void main(String args[])
	{
		PhantomFirst test = new PhantomFirst(500,500);

		test.show();

		Grid2D sinogram = test.CreateSinogram();
		sinogram.show();
	}

}