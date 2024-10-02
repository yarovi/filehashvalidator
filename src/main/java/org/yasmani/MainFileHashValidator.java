package org.yasmani;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Main class for validating file hashes. This class calculates the hash of a
 * given file, compares it with a previously stored hash, and optionally updates
 * the stored hash if the file has been modified.
 */
public class MainFileHashValidator {

	/**
	 * Main method to run the file hash validation.
	 * 
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		// Name of the file in src/main/resources
		String fileName = "original.txt";
		FileHashValidator fileHashValidator = new FileHashValidator();
		System.out.println("FileHashValidator ..2024");

		// Path where the calculated hash will be stored (outside of resources, e.g., in
		// target)
		String outputHashFilePath = "target/archivo_hash.txt";

		try {
			// Use ClassLoader to load the file from src/main/resources
			ClassLoader classLoader = FileHashValidator.class.getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			String filePath = file.getAbsolutePath();

			// Calculate the hash of the current file
			byte[] fileHash = fileHashValidator.calculateFileHash(filePath);
			String fileHashHex = fileHashValidator.bytesToHex(fileHash);
			System.out.println("Hash calculated for the current file: " + fileHashHex);

			// Check if the saved hash file exists
			File hashFile = new File(outputHashFilePath);
			if (hashFile.exists()) {
				// Verify if the current file hash matches the saved one
				boolean isValid = fileHashValidator.verifyFileHash(filePath, outputHashFilePath);
				if (isValid) {
					System.out.println("The file has not been modified.");
				} else {
					// Prompt the user to update the saved hash if the file has been modified
					System.out.println("The file has been modified. Do you want to update the saved hash? (yes/no): ");
					Scanner scanner = new Scanner(System.in);
					String response = scanner.nextLine();

					if (response.equalsIgnoreCase("yes")) {
						// Update the saved hash with the new one
						fileHashValidator.saveHashToFile(fileHashHex, outputHashFilePath);
						System.out.println("Hash updated at: " + outputHashFilePath);
					} else {
						System.out.println("The hash was not updated.");
					}
					scanner.close();
				}
			} else {
				// If the hash file does not exist, save the new hash for the first time
				System.out.println("No saved hash found. Saving the new hash.");
				fileHashValidator.saveHashToFile(fileHashHex, outputHashFilePath);
				System.out.println("Hash saved at: " + outputHashFilePath);
			}

		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
