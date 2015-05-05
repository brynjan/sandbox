package no.progconsult.lambda;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.function.Consumer;

import static org.testng.Assert.*;

/**
 * @author bno
 */
public class ConsumerTest {

    @org.testng.annotations.Test
    public void testAccept() throws Exception {
        List<Point> points = new ArrayList<Point>();
        Point p1 = new Point(1,2);
        Point p2 = new Point(3,4);
        points.add(p1);
        points.add(p2);

        points.forEach(new Consumer<Point>() {
            public void accept(Point p) {
                p.move(p.y, p.x);
            }
        });
        System.out.println("");

        points.forEach(p -> p.move(p.y, p.x));
        System.out.println("");

        Collections.<Point>sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return 0;
            }
        });


    }
}