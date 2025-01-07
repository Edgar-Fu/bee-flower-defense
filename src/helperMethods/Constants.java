package src.helperMethods;

import java.util.List;

public class Constants {

    public static class Towers{
        public static final int WORKER = 0;
        public static final int FIRE = 1;
        public static final int FROST = 2;
        public static final int QUEEN = 3;

        public static String getTowerName(int towerType){
            switch (towerType) {
                case WORKER:
                    return "Worker Bee";
                case FIRE:
                    return "Fire Bee";
                case FROST:
                    return "Frost Bee";
                case QUEEN:
                    return "Queen Bee";
                default:
                    return "";
            }
        }

        public static float GetStartDmg(int towerType){
            switch (towerType) {
                case WORKER:
                    return 5;
                case FIRE:
                    return 35;
                case FROST:
                    return 15;
                case QUEEN:
                    return 50;
                default:
                    return 0;
            }
        }
        public static float GetDefaultRange(int towerType){
            switch (towerType) {
                case WORKER:
                    return 100;
                case FIRE:
                    return 100;
                case FROST:
                    return 100;
                case QUEEN:
                    return 100;
                default:
                    return 0;
            }
        }
        public static float GetDefaultCooldown(int towerType){
            switch (towerType) {
                case WORKER:
                    return 10;
                case FIRE:
                    return 10;
                case FROST:
                    return 10;
                case QUEEN:
                    return 10;
                default:
                    return 0;
            }
        }
    }

    public static class Direction{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class Enemies{
        public static final int HORNET = 0;
        public static final int BADGER = 1;
        public static final int RACCOON = 2;
        public static final int BEAR = 3;

        public static float getSpeed(int enemyType){
            switch (enemyType) {
                case HORNET:
                    return 1.5f;
                case BADGER:
                    return 1f;
                case RACCOON:
                    return 0.8f;
                case BEAR:
                    return 0.6f;
                default:
                    return 0;
            }
        }

        public static int GetStartHealth(int enemyType){
            switch (enemyType) {
                case HORNET:
                    return 50;
                case BADGER:
                    return 80;
                case RACCOON:
                    return 75;
                case BEAR:
                    return 100;
                default:
                    return 0;
            }
        }

    }
    //list of tile ids that will represent the path for enemies to follow
    public static class Tiles{
        public static final List<Integer> pathTiles = List.of(726, 727, 728, 729);
        public static final List<Integer> grassTiles = List.of(181, 182, 183);
    }
    

}
