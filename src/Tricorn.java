import java.awt.geom.Rectangle2D;
public class Tricorn extends FractalGenerator
{
    private Complex z =
            new Complex(0, 0);
    private Complex c =
            new Complex(0, 0);
    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.setRect(-2, -2, 4,4);
    }
    public int numIterations(double x, double y)
    {
        z.real = 0; z.ipic = 0; c.real = x; c.ipic = y;
        for (int IterNum = 0; IterNum < MAX_ITERATIONS; IterNum++)
        {
            z = z.sopr().times(z.sopr()).sum(c);
            if (z.abs() > 4) return IterNum;
        }
        return -1;
    }
    @Override
    public String toString()
    {
        return "Tricorn";
    }
}
