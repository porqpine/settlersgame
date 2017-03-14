const TILE_SIZE = 25;

const SCREEN_WIDTH_PIXELS = 900;
const SCREEN_HEIGHT_PIXELS = 700;

const MAP_WIDTH_TILES = 30;
const MAP_HEIGHT_TILES = 30;

const COLORS = {
    "FOREST": "#007700", "MOUNTAIN": "#666666", "WATER": "blue", "PASTURE": "#66aa00", "DESERT": "#aa8800"
};

const IMAGE_MAP = {
    HOME: new PreloadedImage("img/home.png", 256, 224),
    EMITTER: new PreloadedImage("img/emitter.png", 214, 199),
    SIPHON: new PreloadedImage("img/siphon.png", 256, 170),
    WALL: new PreloadedImage("img/wall.png", 153, 153),
};

function hexToRGBA(hex, opacity) {
    var r = parseInt(hex.substr(1, 2), 16);
    var g = parseInt(hex.substr(3, 2), 16);
    var b = parseInt(hex.substr(5, 2), 16);
    return `rgba(${r},${g},${b},${opacity})`;
}

function isInside(point, vs) {
    // ray-casting algorithm based on
    // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html

    var x = point[0], y = point[1];

    var inside = false;
    for (var i = 0, j = vs.length - 1; i < vs.length; j = i++) {
        var xi = vs[i][0], yi = vs[i][1];
        var xj = vs[j][0], yj = vs[j][1];

        var intersect = ((yi > y) != (yj > y))
            && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }

    return inside;
}