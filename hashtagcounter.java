
import java.io.*;
import java.util.HashMap;


public class hashtagcounter {
	
	public static void main(String[] args) {

		if(args.length == 0){
			System.out.println("No input file");
			return;
		}


		// Reading Input File
		try (BufferedReader bR = new BufferedReader(
				new FileReader(System.getProperty("user.dir")+"/"+args[0]))) {

			FileWriter fw=new FileWriter(new File(System.getProperty("user.dir"))+"/output_file.txt");
			BufferedWriter bw=new BufferedWriter(fw);

			String line = "";

			// Create Fibonacci Max heap
			FibonacciHeap maxHeap = new FibonacciHeap();
			
			// Create Hashmap
			HashMap<String, Node> Hashtags = new HashMap<>();


			while ((line = bR.readLine()) != null) {

	if (line.charAt(0) == '#') {

					/* NUMBER: in this case give output
					   traverse heap
					   -get max
					 */
					String tag = line.split(" ")[0].replaceAll("#", "").trim();
					int occurence = Integer.parseInt(line.split(" ")[1].trim());

					// if hashtag already exists.
					if (Hashtags.containsKey(tag)) {
						// Increase KEY: increment count of related node
						maxHeap.increaseKey(Hashtags.get(tag), (Hashtags.get(tag).getKey() + occurence));

					} else {
						//create new node and insert it into the heap
						Node newNode = maxHeap.insert(occurence, tag);

						//insert the node into the hashmap
						Hashtags.put(tag, newNode);

					}
				}else {

					//exit sequence
					if (line.trim().equalsIgnoreCase("STOP")) {
						break;
					} else {
						
						int count=Integer.parseInt(line.trim());
						if(count > maxHeap.getSize()){
							System.out.println("Invalid Query");
							bR.close();
							bw.close();
							return;
						}

						Node[] teNode = new Node[count];

						//extract max count times
						for (int i = 0; i < count; i++) {

							teNode[i]=maxHeap.extractMax();
							bw.append(teNode[i].getTag());
							if(i < count -1) {
								bw.append(",");
							}

						}

						bw.newLine();
						for (int i = 0; i < count; i++) {
							Hashtags.replace(teNode[i].getTag(),maxHeap.insert(teNode[i].getKey(), teNode[i].getTag()));
						}

					}

				}

			}

			bR.close();
			bw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
