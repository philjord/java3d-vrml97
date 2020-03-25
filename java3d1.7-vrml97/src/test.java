import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.loaders.Scene;

public class test {

	public static void main(String[] args) {
		VrmlLoader vl = new VrmlLoader();
		try {
			// fill test with anything from here
			//https://www.web3d.org/documents/specifications/14772/V2.0/part1/examples.html
			// some things work some things don't
			Scene scene = vl.load("src/test.wrl");
			BranchGroup bg = scene.getSceneGroup();
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

}
