package com.bainiu.image;

/**
 * @author yf 2020/11/12 8:19 下午
 */

public class TwoColorCompare {

    /**
     * k:19,26,40,v:3169
     * #131a28
     * k:17,24,38,v:3162
     * #111826
     * k:21,27,41,v:2992
     * #151b29
     * k:16,23,37,v:2763
     * #101725
     * #101725
     * @param args
     */
    public static void main(String[] args) {
        int[] lab = new int[3];
        int[] color1 = new int[]{51,68,55};
        int[] color2 = new int[]{64,74,63};
//        compareColors(r1,g1,b1,r2,g2,b2);
        double p = compareColors1(color1,color2);
//        log.info("percent:{}",p);
        double[] lab1 = rgbToLab(color1);
        double[] lab2 = rgbToLab(color2);
        double delta = delta(lab1,lab2);
        double delta94 = delta94(lab1,lab2);
        double deltaE2000 = calculateDeltaE2000(lab1,lab2);
//        log.info("lab1:{}",lab1);
//        log.info("lab2:{}",lab2);
//        log.info("delta: {}",delta);
//        log.info("delta94: {}",delta94);
//        log.info("deltaE2000: {}",deltaE2000);


    }


    public static void compareColors(int r1, int g1, int b1,int r2, int g2, int b2){
        // d=sqrt((r2-r1)^2+(g2-g1)^2+(b2-b1)^2)
        // p=d/sqrt((255)^2+(255)^2+(255)^2)
       double d = Math.sqrt(Math.pow(r2-r1,2) + Math.pow(g2-g1,2) + Math.pow(b2-b1,2));
       double a = Math.sqrt(Math.pow(255,2)*3);
       double p = d / a;
       System.out.println(p);
    }

    public static double compareColors1(int[] color1,int[] color2){
        // d=sqrt((r2-r1)^2+(g2-g1)^2+(b2-b1)^2)
        // p=d/sqrt((255)^2+(255)^2+(255)^2)
        double sum = 0;
        for (int i = 0; i < 3 ; i++) {
            sum += Math.pow(color2[i] - color1[i],2);
        }
        double d = Math.sqrt(sum);
        double a = Math.sqrt(Math.pow(255,2)*3);
        double p = d / a;
//        System.out.println(1-p);
        return 1-p;
    }

    public static int[] rgb2lab(int[] color ,int[] lab) {
        //http://www.brucelindbloom.com
        int R = color[0];
        int G = color[1];
        int B = color[2];
        float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
        float Ls, as, bs;
        float eps = 216.f/24389.f;
        float k = 24389.f/27.f;

        float Xr = 0.964221f;  // reference white D50
        float Yr = 1.0f;
        float Zr = 0.825211f;

        // RGB to XYZ
        r = R/255.f; //R 0..1
        g = G/255.f; //G 0..1
        b = B/255.f; //B 0..1

        // assuming sRGB (D65)
        if (r <= 0.04045)
            r = r/12;
        else
            r = (float) Math.pow((r+0.055)/1.055,2.4);

        if (g <= 0.04045)
            g = g/12;
        else
            g = (float) Math.pow((g+0.055)/1.055,2.4);

        if (b <= 0.04045)
            b = b/12;
        else
            b = (float) Math.pow((b+0.055)/1.055,2.4);


        X =  0.436052025f*r     + 0.385081593f*g + 0.143087414f *b;
        Y =  0.222491598f*r     + 0.71688606f *g + 0.060621486f *b;
        Z =  0.013929122f*r     + 0.097097002f*g + 0.71418547f  *b;

        // XYZ to Lab
        xr = X/Xr;
        yr = Y/Yr;
        zr = Z/Zr;

        if ( xr > eps ){
            fx =  (float) Math.pow(xr, 1/3.);
        } else{
            fx = (float) ((k * xr + 16.) / 116.);
        }

        if ( yr > eps ){
            fy =  (float) Math.pow(yr, 1/3.);
        } else{
            fy = (float) ((k * yr + 16.) / 116.);
        }

        if ( zr > eps ){
            fz =  (float) Math.pow(zr, 1/3.);
        } else{
            fz = (float) ((k * zr + 16.) / 116);
        }

        Ls = ( 116 * fy ) - 16;
        as = 500*(fx-fy);
        bs = 200*(fy-fz);

        lab[0] = (int) (2.55*Ls + .5);
        lab[1] = (int) (as + .5);
        lab[2] = (int) (bs + .5);
        return lab;
    }

    public static double[] rgbToLab(int[] color) {
        int R = color[0];
        int G = color[1];
        int B = color[2];

        double r, g, b, X, Y, Z, xr, yr, zr;

        // D65/2°
        double Xr = 95.047;
        double Yr = 100.0;
        double Zr = 108.883;


        // --------- RGB to XYZ ---------//

        r = R/255.0;
        g = G/255.0;
        b = B/255.0;

        if (r > 0.04045)
            r = Math.pow((r+0.055)/1.055,2.4);
        else
            r = r/12.92;

        if (g > 0.04045)
            g = Math.pow((g+0.055)/1.055,2.4);
        else
            g = g/12.92;

        if (b > 0.04045)
            b = Math.pow((b+0.055)/1.055,2.4);
        else
            b = b/12.92 ;

        r*=100;
        g*=100;
        b*=100;

        X =  0.4124*r + 0.3576*g + 0.1805*b;
        Y =  0.2126*r + 0.7152*g + 0.0722*b;
        Z =  0.0193*r + 0.1192*g + 0.9505*b;


        // --------- XYZ to Lab --------- //

        xr = X/Xr;
        yr = Y/Yr;
        zr = Z/Zr;

        if ( xr > 0.008856 )
            xr =  (float) Math.pow(xr, 1/3.);
        else
            xr = (float) ((7.787 * xr) + 16 / 116.0);

        if ( yr > 0.008856 )
            yr =  (float) Math.pow(yr, 1/3.);
        else
            yr = (float) ((7.787 * yr) + 16 / 116.0);

        if ( zr > 0.008856 )
            zr =  (float) Math.pow(zr, 1/3.);
        else
            zr = (float) ((7.787 * zr) + 16 / 116.0);


        double[] lab = new double[3];

        lab[0] = (116*yr)-16;
        lab[1] = 500*(xr-yr);
        lab[2] = 200*(yr-zr);

        return lab;

    }

    public static double delta94(double[] lab1,double[] lab2){
        double l1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];
        double l2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];
        double xC1 = Math.sqrt(Math.pow(a1,2) + Math.pow(b1,2));
        double xC2 = Math.sqrt(Math.pow(a2,2) + Math.pow(b2,2));
        double xDL = l2 -l1;
        double xDC = xC2 - xC1;
        double xDE = Math.sqrt(Math.pow(l1-l2,2) + Math.pow(a1-a2,2) + Math.pow(b1-b2,2));
        double xDH = Math.pow(xDE,2) - Math.pow(xDL,2) - Math.pow(xDC,2);
        if (xDH > 0){
            xDH = Math.sqrt(xDH);
        }else {
            xDH = 0;
        }
        double xSC = 1 + ( 0.045 * xC1 );
        double xSH = 1 + ( 0.015 * xC1 );
        return Math.sqrt(Math.pow(xDL,2) + Math.pow(xDC,2) + Math.pow(xDH,2));
    }

    public static double delta(double[] lab1,double[] lab2){
        double l1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];
        double l2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];
        return Math.sqrt(Math.pow(l1-l2,2) + Math.pow(a1-a2,2) + Math.pow(b1-b2,2));
    }

    public static double calculateDeltaE2000(double[] lab1, double[] lab2) {
        double l1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];
        double l2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];
        double Lmean = (l1 + l2) / 2.0;
        double c1 =  Math.sqrt(a1*a1 + b1*b1);
        double c2 =  Math.sqrt(a2*a2 + b2*b2);
        double Cmean = (c1 + c2) / 2.0;

        double G =  ( 1 - Math.sqrt( Math.pow(Cmean, 7) / (Math.pow(Cmean, 7) + Math.pow(25, 7)) ) ) / 2;
        double a1prime = a1 * (1 + G);
        double a2prime = a2 * (1 + G);

        double C1prime =  Math.sqrt(a1prime*a1prime + b1*b1); 
        double C2prime =  Math.sqrt(a2prime*a2prime + b2*b2); 
        double Cmeanprime = (C1prime + C2prime) / 2; 

        double h1prime =  Math.atan2(b1, a1prime) + 2*Math.PI * (Math.atan2(b1, a1prime)<0 ? 1 : 0);
        double h2prime =  Math.atan2(b2, a2prime) + 2*Math.PI * (Math.atan2(b2, a2prime)<0 ? 1 : 0);
        double Hmeanprime =  ((Math.abs(h1prime - h2prime) > Math.PI) ? (h1prime + h2prime + 2*Math.PI) / 2 : (h1prime + h2prime) / 2); 

        double T =  1.0 - 0.17 * Math.cos(Hmeanprime - Math.PI/6.0) + 0.24 * Math.cos(2*Hmeanprime) + 0.32 * Math.cos(3*Hmeanprime + Math.PI/30) - 0.2 * Math.cos(4*Hmeanprime - 21*Math.PI/60); 

        double deltahprime =  ((Math.abs(h1prime - h2prime) <= Math.PI) ? h2prime - h1prime : (h2prime <= h1prime) ? h2prime - h1prime + 2*Math.PI : h2prime - h1prime - 2*Math.PI); 

        double deltaLprime = l2 - l1;
        double deltaCprime = C2prime - C1prime; 
        double deltaHprime =  2.0 * Math.sqrt(C1prime*C2prime) * Math.sin(deltahprime / 2.0); 
        double SL =  1.0 + ( (0.015*(Lmean - 50)*(Lmean - 50)) / (Math.sqrt( 20 + (Lmean - 50)*(Lmean - 50) )) );
        double SC =  1.0 + 0.045 * Cmeanprime; 
        double SH =  1.0 + 0.015 * Cmeanprime * T; 

        double deltaTheta =  (30 * Math.PI / 180) * Math.exp(-((180/Math.PI*Hmeanprime-275)/25)*((180/Math.PI*Hmeanprime-275)/25));
        double RC =  (2 * Math.sqrt(Math.pow(Cmeanprime, 7) / (Math.pow(Cmeanprime, 7) + Math.pow(25, 7))));
        double RT =  (-RC * Math.sin(2 * deltaTheta));

        double KL = 1;
        double KC = 1;
        double KH = 1;

        return Math.sqrt(
                ((deltaLprime/(KL*SL)) * (deltaLprime/(KL*SL))) +
                        ((deltaCprime/(KC*SC)) * (deltaCprime/(KC*SC))) +
                        ((deltaHprime/(KH*SH)) * (deltaHprime/(KH*SH))) +
                        (RT * (deltaCprime/(KC*SC)) * (deltaHprime/(KH*SH)))
        );
    }


}
