
public class Animal {

	public static void main(String[] args) {
		// main empty.
	}
}

abstract class Mammal extends Animal {
	public abstract String bark();
	public abstract String getFood();
}

abstract class Reptile extends Animal {
    public abstract String getFood();
}

final class Cat extends Mammal {
    private String name;
    private float weight;
    private String nameSlave;
    
    public Cat(String name, float weight, String nameSlave) {
        this.name = name;
        this.weight = weight;
        this.nameSlave = nameSlave;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }
    
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getNameSlave() {
        return nameSlave;
    }

    public void setNameSlave(String nameSlave) {
        this.nameSlave = nameSlave;
    }
    
    @Override
    public String bark() {
        System.out.println("Meow");
        return "Meow";
    }
    
    @Override
    public String getFood() {
        System.out.println("Fish");
        return "Fish";
    }
}


final class Dog extends Mammal {
    private String name;
    private float weight;
    private String nameMaster;

    // Field of the class should be initiated by Constructor
    public Dog(String name, float weight, String nameMaster) {
        this.name = name;
        this.weight = weight;
        this.nameMaster = nameMaster;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getNameMaster() {
        return nameMaster;
    }

    public void setNameMaster(String nameMaster) {
        this.nameMaster = nameMaster;
    }
    
    @Override
    public String bark() {
        System.out.println("Bowbow");
        return "Bowbow";
    }
    
    @Override
    public String getFood() {
        System.out.println("Apple");
        return "Apple";
    }
}

final class Crocodile extends Reptile {
    private String name;
    private float weight;
    
    public Crocodile(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
    
    @Override
    public String getFood() {
        System.out.println("Meat");
        return "Meat";
    }
}