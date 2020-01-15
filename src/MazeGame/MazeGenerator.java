package MazeGame;

import MazeGame.rooms.*;

import java.util.Random;

import static MazeGame.Info.roomSize;

public class MazeGenerator {

    private int mapSize;
    private Room[][] rooms;
    private Cell[][] totalMap;
    private Random random = new Random();

    MazeGenerator(int mapSize) {
        this.mapSize = mapSize;
        rooms = new Room[mapSize][mapSize];
        totalMap = new Cell[mapSize * roomSize][mapSize * roomSize];
    }

    public void generateRooms() {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                boolean[] requirements = new boolean[8]; // top, right, bot, left || from open to close
                for (int k = 0; k < 8; k++) {
                    requirements[k] = false;
                }

                // boundary requirements
                if (i == 0) {
                    // top close
                    requirements[4] = true;
                }

                if (i == mapSize - 1) {
                    // bot close
                    requirements[6] = true;
                }

                if (j == 0) {
                    // left close
                    requirements[7] = true;
                }

                if (j == mapSize - 1) {
                    // right close
                    requirements[5] = true;
                }

                // previous room requirements
                if (i != 0) {
                    // top room requirements
                    if (rooms[i - 1][j].getOpenBot()) {
                        // top room open bot, we need open top
                        requirements[0] = true;
                    }
                }

                if (j != 0) {
                    // left room requirements
                    if (rooms[i][j - 1].getOpenRight()) {
                        requirements[3] = true;
                    }
                }

                int randomRoomNum;
                while (true) {
                    // current have 16 rooms
                    randomRoomNum = random.nextInt(15);

                    if (isRoomValid(randomRoomNum, requirements)) {
                        break;
                    }
                }

                // generate the corresponding room
                rooms[i][j] = generateRoom(randomRoomNum);
            }
        }
        rememberRooms();
    }

    private void rememberRooms() {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                // cell loop
                for (int k = 0; k < roomSize; k++) {
                    for (int h = 0; h < roomSize; h++) {
                        rooms[i][j].cells[k][h].setX(i * roomSize + k);
                        rooms[i][j].cells[k][h].setY(j * roomSize + h);
                        totalMap[i * roomSize + k][j * roomSize + h] = rooms[i][j].cells[k][h];
                    }
                }
            }
        }
    }

    private boolean isRoomValid(int roomNumber, boolean[] requirements) {
        boolean openTop = false;
        boolean openBot = false;
        boolean openLeft = false;
        boolean openRight = false;
        if (roomNumber == 0) {
            // room corner 1
            openBot = true;
            openRight = true;
        } else if (roomNumber == 1) {
            // room corner 2
            openBot = true;
            openLeft = true;
        } else if (roomNumber == 2) {
            // room corner 3
            openTop = true;
            openLeft = true;
        } else if (roomNumber == 3) {
            // room corner 4
            openTop = true;
            openRight = true;
        } else if (roomNumber == 4) {
            // room cross
            openBot = true;
            openLeft = true;
            openRight = true;
            openTop = true;
        } else if (roomNumber == 5) {
            // room I left
            openLeft = true;
            openRight = true;
        } else if (roomNumber == 6) {
            // room I top
            openTop = true;
            openBot = true;
        } else if (roomNumber == 7) {
            // room T 1
            openBot = true;
            openRight = true;
            openLeft = true;
        } else if (roomNumber == 8) {
            // room T 2
            openTop = true;
            openRight = true;
            openLeft = true;
        } else if (roomNumber == 9) {
            // room T 3
            openTop = true;
            openBot = true;
            openLeft = true;
        } else if (roomNumber == 10) {
            // room T 4
            openTop = true;
            openBot = true;
            openRight = true;
        } else if (roomNumber == 11) {
            // room Wall 1
            openLeft = true;
            openBot = true;
            openRight = true;
        } else if (roomNumber == 12) {
            // room Wall 2
            openTop = true;
            openBot = true;
            openLeft = true;
        } else if (roomNumber == 13) {
            // room Wall 3
            openTop = true;
            openLeft = true;
            openRight = true;
        } else if (roomNumber == 14) {
            // room Wall 4
            openTop = true;
            openBot = true;
            openRight = true;
        } else if (roomNumber == 15) {
            // room All Open
            openTop = true;
            openBot = true;
            openRight = true;
            openLeft = true;
        } else if (roomNumber == 16) {
            // room Wall 5
            openRight = true;
            openLeft = true;
        } else if (roomNumber == 17) {
            // room Wall 6
            openTop = true;
            openBot = true;
        }

        if (requirements[0]) {
            if (!openTop) {
                return false;
            }
        }

        if (requirements[1]) {
            if (!openRight) {
                return false;
            }
        }

        if (requirements[2]) {
            if (!openBot) {
                return false;
            }
        }

        if (requirements[3]) {
            if (!openLeft) {
                return false;
            }
        }

        if (requirements[4]) {
            if (openTop) {
                return false;
            }
        }

        if (requirements[5]) {
            if (openRight) {
                return false;
            }
        }

        if (requirements[6]) {
            if (openBot) {
                return false;
            }
        }

        if (requirements[7]) {
            if (openLeft) {
                return false;
            }
        }
        return true;
    }

    private Room generateRoom(int roomNumber) {
        if (roomNumber == 0) {
            // room corner 1
            return new Room_Corner_1();
        } else if (roomNumber == 1) {
            // room corner 2
            return new Room_Corner_2();
        } else if (roomNumber == 2) {
            // room corner 3
            return new Room_Corner_3();
        } else if (roomNumber == 3) {
            // room corner 4
            return new Room_Corner_4();
        } else if (roomNumber == 4) {
            // room cross
            return new Room_Cross();
        } else if (roomNumber == 5) {
            // room I left
            return new Room_I_Left();
        } else if (roomNumber == 6) {
            // room I top
            return new Room_I_Top();
        } else if (roomNumber == 7) {
            // room T 1
            return new Room_T_1();
        } else if (roomNumber == 8) {
            // room T 2
            return new Room_T_2();
        } else if (roomNumber == 9) {
            // room T 3
            return new Room_T_3();
        } else if (roomNumber == 10) {
            // room T 4
            return new Room_T_4();
        } else if (roomNumber == 11) {
            // room Wall 1
            return new Room_Wall_1();
        } else if (roomNumber == 12) {
            // room Wall 2
            return new Room_Wall_2();
        } else if (roomNumber == 13) {
            // room Wall 3
            return new Room_Wall_3();
        } else if (roomNumber == 14) {
            // room Wall 4
            return new Room_Wall_4();
        } else if (roomNumber == 15) {
            // room All Open
            return new Room_All_Open();
        } else if (roomNumber == 16) {
            // room Wall 5
            return new Room_Wall_5();
        } else if (roomNumber == 17) {
            // room Wall 6
            return new Room_Wall_6();
        } else {
            return new Room();
        }
    }

    public Cell[][] getTotalMap() {
        return totalMap;
    }

    public Room[][] getRooms() {
        return rooms;
    }
}
