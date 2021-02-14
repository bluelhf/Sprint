package io.github.bluelhf.sprintpls.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Aligns {

    public static abstract class Alignment {
        private final LinkedHashMap<Area, Align> alignmentMap = new LinkedHashMap<>();

        public Alignment() {
            alignmentMap.putAll(getAlignmentMap());
        }

        protected abstract LinkedHashMap<Area, Align> getAlignmentMap();

        public Set<Area> getAreas() {
            refresh();
            return alignmentMap.keySet();
        }

        private void refresh() {
            alignmentMap.clear();
            alignmentMap.putAll(getAlignmentMap());
        }

        public Align getAlignment(int x, int y) {
            refresh();
            Align align = Align.CENTRE;
            @SuppressWarnings("unchecked") // We know the type of alignmentMap because we defined it
            Map.Entry<Area, Align>[] entrySet = alignmentMap.entrySet().toArray(new Map.Entry[0]);

            for (int i = entrySet.length - 1; i >= 0; i--) {
                Entry<Area, Align> e = entrySet[i];

                if (e.getKey().isWithin(x, y)) {
                    align = e.getValue();
                    break;
                }
            }

            return align;
        }
    }

    public static final Alignment INDICATOR_ALIGNMENT = new Alignment() {
        @Override
        protected LinkedHashMap<Area, Align> getAlignmentMap() {
            LinkedHashMap<Area, Align> map = new LinkedHashMap<>();
            int width = MathsUtility.getScaledWidth();
            int height = MathsUtility.getScaledHeight();
            Area hotbar = MathsUtility.getHotbarBounds();

            // Because of the way we preserve the order of areas, we can overlap them and the last one will always be chosen.
            map.put(new Area(0, 0, width / 2 - 22, hotbar.getMinY() - 22), Align.LEFT);
            map.put(new Area(0, hotbar.getMinY() - 22, hotbar.getMinX() - 22, height), Align.LEFT);
            map.put(new Area(hotbar.getMaxX() + 22, hotbar.getMinY() - 22, width, height), Align.RIGHT);
            map.put(new Area(width / 2 + 22, 0, width, hotbar.getMinY() - 22), Align.RIGHT);
            map.put(new Area(hotbar.getMinX() - 22, hotbar.getMinY() - 22, width / 2 - 22, height), Align.RIGHT);
            map.put(new Area(width / 2 + 22, hotbar.getMinY() - 22, hotbar.getMaxX() + 22, height), Align.LEFT);
            map.put(new Area(width / 2 - 22, 0, width / 2 + 22, height), Align.CENTRE);

            return map;
        }
    };

    public enum Align {
        LEFT(0),
        CENTRE(1),
        RIGHT(2);

        private final int num;

        Align(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public int mapFromLeft(int x, int width) {
            return (int) (x - width * num * 0.5);
        }

        public static Align forNum(int num) {
            for (Align align : values()) {
                if (align.num == num) return align;
            }
            throw new IllegalArgumentException("No alignment exists for number: " + num);
        }
    }
}
