package io.github.bluelhf.sprint.util;

public class Area {
    private int minX, minY, maxX, maxY;

    public Area(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        swapIfNecessary();
    }


    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    @SuppressWarnings("RedundantIfStatement") // Readability
    public boolean overlaps(Area area) {
        if (this.getMinX() >= area.getMaxX() || area.getMinX() >= this.getMaxX()) return false;
        if (this.getMinY() <= area.getMaxY() || area.getMinY() <= this.getMaxY()) return false;
        return true;
    }

    public boolean isWithin(int x, int y) {
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY;
    }

    private void swapIfNecessary() {
        if (this.minX > this.maxX) {
            int temp = this.minX;
            this.minX = this.maxX;
            this.maxX = temp;
        }
        if (this.minY > this.maxY) {
            int temp = this.minY;
            this.minY = this.maxY;
            this.maxY = temp;
        }
    }
}
