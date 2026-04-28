package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Producto concreto: pago con criptomonedas.
 */
public class CryptoPayment extends PaymentMethod {

    private final String walletAddress;
    private final String cryptoType;
    private final double walletBalance;
    private String blockchainHash;

    public CryptoPayment(double amount, String customerId, String description,
                         String walletAddress, String cryptoType, double walletBalance) {
        super(amount, customerId, description);
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
        this.walletBalance = walletBalance;
    }

    @Override
    public boolean validatePaymentMethod() {
        return validateWalletAddress() && validateBalance();
    }

    private boolean validateWalletAddress() {
        return walletAddress != null && walletAddress.length() >= 26;
    }

    private boolean validateBalance() {
        return walletBalance >= amount;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing Cryptocurrency payment...");

        if (!validatePaymentMethod()) {
            System.out.println("Crypto validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }

        setStatus(PaymentStatus.PROCESSING);

        try {
            this.blockchainHash = generateBlockchainHash();
            System.out.println("Transaction broadcasted to blockchain");
            System.out.println("Blockchain hash: " + blockchainHash);
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "CRYPTOCURRENCY";
    }

    private String generateBlockchainHash() {
        return "0x" + Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    public String getWalletAddress()  { return walletAddress; }
    public String getCryptoType()     { return cryptoType; }
    public String getBlockchainHash() { return blockchainHash; }
    public double getWalletBalance()  { return walletBalance; }
}
