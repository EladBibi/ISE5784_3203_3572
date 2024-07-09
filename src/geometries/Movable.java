package geometries;

import primitives.Point;
import primitives.Vector;

public abstract class Movable {

    protected Point pivot = Point.ZERO;

    public Intersectable setPivot(Point point){
        this.pivot = point;
        return (Intersectable)this;
    }

    public Point getPivot(){
        return pivot;
    }

    public abstract Intersectable moveCloneTo(Point position);

    public abstract Intersectable cloneAndRotate(Vector rotationAxis, double degrees);
}
