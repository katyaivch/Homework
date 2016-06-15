/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework_poligons;

import com.google.gson.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author katya
 */
public class HomeWork_poligons {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get("/home/katya/NetBeansProjects/HomeWork_poligons/src/homework_poligons/poligons.json"),
                    Charset.defaultCharset());

            String fileContents = "";
            for (String line : lines) {
                fileContents += line;
            }

            JsonElement jelement = new JsonParser().parse(fileContents);
            Gson gson = new Gson();
            Polygon[] MyPolygons = gson.fromJson(jelement, Polygon[].class);

            int PolygonCount = MyPolygons.length;

            int GeneralArea = 0;

            for (int i = 0; i < PolygonCount; i++) {
                List<Vertex> commonVertices = new ArrayList<>();
                for (int j = i + 1; j < PolygonCount; j++) {

//берем і полигон и проверяем все точки j полигона на вхождение в i тый
                    for (int l = 0; l < MyPolygons[j].getVerticles().size(); l++) {
                        if (inPolygon(MyPolygons[i],
                                MyPolygons[j].getVerticles().get(l).getX(),
                                MyPolygons[j].getVerticles().get(l).getY())) {
                            commonVertices.add(MyPolygons[j].getVerticles().get(l));
                        }
                    }

                    if (!commonVertices.isEmpty()) {
//берем і полигон и проверяем все точки i полигона на вхождение в j тый  
                        for (int l = 0; l < MyPolygons[i].getVerticles().size(); l++) {
                            if (inPolygon(MyPolygons[j],
                                    MyPolygons[i].getVerticles().get(l).getX(),
                                    MyPolygons[i].getVerticles().get(l).getY())) {
                                commonVertices.add(MyPolygons[i].getVerticles().get(l));
                            }
                        }
                    }

//находим точки пересечения сторон 
                    if (!commonVertices.isEmpty()) {

                        for (int h = 0, l = MyPolygons[i].getVerticles().size() - 1;
                                h < MyPolygons[i].getVerticles().size(); l = h++) {
                            
                            for (int s = 0, m = MyPolygons[j].getVerticles().size() - 1;
                                    s < MyPolygons[j].getVerticles().size(); m = s++) {
                                
                                Vertex temp = new Vertex();

                                Vertex iFirst = MyPolygons[i].getVerticles().get(h);
                                Vertex iSecond = MyPolygons[i].getVerticles().get(l);
                                Vertex jFirst = MyPolygons[j].getVerticles().get(s);
                                Vertex jSecond = MyPolygons[j].getVerticles().get(m);

                                temp = Intersection(iFirst, iSecond, jFirst, jSecond);

                                if (temp != null) {
                                    if (onLine(jFirst, jSecond, temp)) {
                                        for (int g = 0; g < commonVertices.size(); g++) {
                                            if (!temp.VertexEquals(commonVertices.get(g))) {
                                                commonVertices.add(temp);
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
                GeneralArea += AreaCalculation(commonVertices);
            }
            System.out.println("Common area = " + GeneralArea);
        } catch (IOException ex) {

            Logger.getLogger(HomeWork_poligons.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public static boolean onLine(Vertex vert1, Vertex vert2, Vertex current) {
        int x1 = vert1.getX();
        int x2 = vert2.getX();
        int x = current.getX();

        int y1 = vert1.getY();
        int y2 = vert2.getY();
        int y = current.getY();

        boolean between01;
        between01 = false;

        float p1 = -1;
        float p2 = -2;
        float znam1 = (x1 - x2);
        float znam2 = (y1 - y2);
        if (znam1 != 0 && znam2 != 0) {
            p1 = (x - x2) / znam1;
            p2 = (y - y2) / znam2;
        }

        if (p1 >= 0 && p2 <= 1 && p2 >= 0 && p2 <= 1) {
            between01 = true;
        }

        return p1 == p2 && between01;
    }

    public static Vertex Intersection(Vertex vert1, Vertex vert2, Vertex vert3, Vertex vert4) {

        int x1 = vert1.getX();
        int x2 = vert2.getX();
        int x3 = vert3.getX();
        int x4 = vert4.getX();

        int y1 = vert1.getY();
        int y2 = vert2.getY();
        int y3 = vert3.getY();
        int y4 = vert4.getY();

        int u1;
        int u2;

        int u;
        Vertex res = new Vertex();

        u1 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        u2 = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

        if (u2 != 0) {
            u = (int) (u1 / u2);
            res.setX(x1 + u * (x2 - x1));
            res.setY(y1 + u * (y2 - y1));
        } else {
            return null;
        }

        return res;
    }

    public static boolean inPolygon(Polygon currentPoly, int x, int y) {
        boolean result = false;
        int count = currentPoly.getVerticles().size();
        List<Vertex> currentVert = currentPoly.getVerticles();

        for (int i = 0, j = count - 1; i < count; j = i++) {

            int iX = currentVert.get(i).getX();
            int iY = currentVert.get(i).getY();
            int jX = currentVert.get(j).getX();
            int jY = currentVert.get(j).getY();

            if ((((iY <= y) && (y < jY)) || ((jY <= y) && (y < iY)))
                    && (x > (jX - iX) * (y - iY) / (jY - iY) + iX)) {
                result = !result;
            }

        }

        return result;
    }

    public static int AreaCalculation(List<Vertex> vertices) {
        int area = 0;

        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
            area += (vertices.get(j).getX() + vertices.get(i).getX())
                    * (vertices.get(j).getY() - vertices.get(i).getY());

        }
        return Math.abs(area / 2);
    }

}
