/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework_poligons;

/**
 *
 * @author katya
 */
public class Vertex {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean VertexEquals(Vertex a) {
        return a.getX() == this.getX() && a.getY() == this.getY();
    }
;

}
