import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;



/**
 *
 * When activated, this class listens for any change on the updoad directory, in case a new file was added to that upload directory
 * a new file Tarcker is instanciated, and the application starts automatically seeding that file
 *
 * @author Adem Hmama
 * @version 1.0
 *
 */
public class UploadListener implements Runnable {

	@Override
	public void run() {
		Set<String> added = new HashSet<>();
		while(true){

			// collect all the currently tracked files
			Set<String> metaFiles = new HashSet<>();
			File metaDir = new File(Peer.metaPath);
			for (File meta : metaDir.listFiles()) {
				if (meta.isDirectory())
					continue;
				String fileName = meta.getName().substring(0, meta.getName().lastIndexOf("."));
				metaFiles.add(fileName);
			}


			File uploadDir = new File(Peer.uploadPath);
			// TODO: do some checking, user might delete the directory, this should leed into an error resluting in listener shutdown


			for (File fl : uploadDir.listFiles()) {
				if(fl.isDirectory())
					continue;
				if (!metaFiles.contains(fl.getName())) {
					// init new filetracker
					try {
						
						if(!added.contains(fl.getName())){
							FileTracker newFileTracker = new FileTracker(fl.getName(),fl.getAbsolutePath());
							Peer.fileTrackers.put(newFileTracker.getKey(), newFileTracker);
							added.add(fl.getName());
							// TODO: fix this by setting a uniform hash function
						}
						
						
						
						
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			try {
				// TODO: find another more appropriate way to listen to a directory (there has to be a java function for this)
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}