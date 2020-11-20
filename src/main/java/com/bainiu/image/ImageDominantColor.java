package com.bainiu.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: yf
 * @date: 2020/11/20  09:39
 * @desc:
 */

public class ImageDominantColor {

    private static double multi = 0;

    private static List<MyColor> myColorList = new ArrayList<>();



    public static List<MyColor> getHexColor(BufferedImage image) {
        myColorList = new ArrayList<>();
        Map<Integer, Integer> colorMap = new HashMap<>(16);
        int height = image.getHeight();
        int width = image.getWidth();
        multi = height * width;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i, j);
                Integer counter = colorMap.get(pixel);
                if (counter == null) {
                    counter = 0;
                }
                colorMap.put(pixel, ++counter);
            }
        }

        return getMostCommonColor(colorMap);
    }

    private static List<MyColor> getMostCommonColor(Map<Integer, Integer> map){
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());

        list.sort((Map.Entry<Integer, Integer> obj1, Map.Entry<Integer, Integer> obj2)
                -> ((Comparable) obj2.getValue()).compareTo(obj1.getValue()));

        List<Map.Entry<Integer, Integer>> list1 = combineColor(list);
        // maybe endless loop
        while (list1.size() > 1 ){
            list1 = combineColor(list1);
        }
        return myColorList;
    }

    public static List<Map.Entry<Integer, Integer>> combineColor(List<Map.Entry<Integer, Integer>> list){
        List<Map.Entry<Integer, Integer>> list1 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Map.Entry<Integer, Integer> preEntry = list.get(i);
            int[] rgb1 = getRGBArr(preEntry.getKey());
            int sum = preEntry.getValue();
            for (int j = i+1; j < list.size(); j++) {
                Map.Entry<Integer, Integer> nextEntry = list.get(j);
                int[] rgb2 = getRGBArr(nextEntry.getKey());
                double deltaE = TwoColorCompare.calculateDeltaE2000(TwoColorCompare.rgbToLab(rgb1), TwoColorCompare.rgbToLab(rgb2));
                if (deltaE < 21){
                    sum += nextEntry.getValue();
                } else {
                    list1.add(nextEntry);
                }
            }
            preEntry.setValue(sum);
            double percent = sum / (multi);
            if (percent < 0.001){
                continue;
            }
            String percentStr = (percent *100+"").substring(0,4)+"%";
            MyColor myColor = new MyColor();
            myColor.setPercentStr(percentStr);
            myColor.setPercent(Double.parseDouble((percent *100+"").substring(0,4)));
            myColor.setRgb(rgb1);
            myColorList.add(myColor);
        }
        return list1;
    }

    private static int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red, green, blue};
    }


    public static void main(String[] args) throws IOException {
//        File file= new File("D:\\modoo-image\\TR190690OC112979D-RR14-12\\784f36fd-39e9-46fb-94f7-b8a645170aa9.out.out.png");
        File file= new File("D:\\modoo-image\\TR170400BS110957D-BB11-12\\2bd21edc-076a-474d-991e-6395d63a153b.jpg");
        BufferedImage img = ImageIO.read(file);
        List<MyColor> colorList = getHexColor(img);
        colorList.sort(Comparator.comparing(MyColor::getPercent).reversed());
        CreateImage.createImage(colorList,"",img);
        System.out.println("colorList:"+ colorList);
    }
}
