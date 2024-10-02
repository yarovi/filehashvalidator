package org.yasmani;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for validating file hashes. This class provides methods to calculate
 * the hash of a file, convert byte arrays to hexadecimal strings, save the hash
 * to a file, and verify if the current file hash matches a saved hash.
 */
public class FileHashValidator {

	/**
	 * Calculates the SHA-256 hash of a file.
	 *
	 * @param filePath The path to the file whose hash needs to be calculated.
	 * @return The calculated hash as a byte array.
	 * @throws IOException              If an I/O error occurs reading the file.
	 * @throws NoSuchAlgorithmException If the specified hash algorithm is not
	 *                                  available.
	 */
	public static byte[] calculateFileHash(String filePath) throws IOException, NoSuchAlgorithmException {
		// Read the file as a byte array
		byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

		// Create an instance of MessageDigest with SHA-256
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		// Calculate the hash of the file
		byte[] fileHash = digest.digest(fileBytes);

		return fileHash;
	}

	/**
	 * Converts a byte array to a hexadecimal string representation.
	 *
	 * @param hash The byte array to convert.
	 * @return The hexadecimal string representation of the byte array.
	 */
	public static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	/**
	 * Saves a hash string to a specified file.
	 *
	 * @param hash           The hash string to save.
	 * @param outputFilePath The path to the output file where the hash will be
	 *                       saved.
	 * @throws IOException If an I/O error occurs writing to the file.
	 */
	public static void saveHashToFile(String hash, String outputFilePath) throws IOException {
		FileWriter writer = new FileWriter(new File(outputFilePath));
		writer.write(hash);
		writer.close();
	}

	/**
	 * Verifies if the current hash of a file matches the saved hash in a specified
	 * file.
	 *
	 * @param filePath          The path to the file whose hash needs to be
	 *                          verified.
	 * @param savedHashFilePath The path to the file containing the saved hash.
	 * @return True if the hashes match, false otherwise.
	 * @throws IOException              If an I/O error occurs reading the files.
	 * @throws NoSuchAlgorithmException If the specified hash algorithm is not
	 *                                  available.
	 */
	public static boolean verifyFileHash(String filePath, String savedHashFilePath)
			throws IOException, NoSuchAlgorithmException {
		// Calculate the current hash of the file
		byte[] currentFileHash = calculateFileHash(filePath);
		String currentFileHashHex = bytesToHex(currentFileHash);

		// Read the saved hash from the hash file
		String savedHash = new String(Files.readAllBytes(Paths.get(savedHashFilePath)));

		// Compare the hashes
		return currentFileHashHex.equals(savedHash.trim());
	}
}
