public class FaceWeight implements Weightable {
    public double weight;

    public FaceWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public FaceWeight add(Weightable weightable) {
        FaceWeight faceWeight = (FaceWeight) weightable;
        return new FaceWeight(this.weight + faceWeight.weight);
    }

    @Override
    public FaceWeight mult(double scalar) {
        return new FaceWeight(this.weight * scalar);
    }

    @Override
    public FaceWeight copy() {
        return new FaceWeight(this.weight);
    }
}
