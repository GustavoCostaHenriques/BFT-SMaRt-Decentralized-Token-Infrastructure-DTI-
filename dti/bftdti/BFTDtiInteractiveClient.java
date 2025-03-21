/**
 * BFT Dti implementation (interactive client).
 *
 */
package dti.bftdti;

import java.io.Console;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class BFTDtiInteractiveClient {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Use: java BFTDtiServer <client id>");
            System.exit(-1);
        }
        int clientId = Integer.parseInt(args[0]);
        BFTDti<Integer, String> bftDti = new BFTDti<>(clientId);

        Console console = System.console();

        System.out.println("\nCommands:\n");
        System.out.println("\n MY_COINS: Retrieve the values from the Coin map");
        System.out.println("\n MINT: Insert value into the map");  
        System.out.println("\n SPEND: Transfer some value of a coin to another client");
        System.out.println("\n MY_NFTS: Retrieve the values from the NFT map");
        System.out.println("\n MINT_NFT: Insert value into the NFT map");
        System.out.println("\n SET_NFT_PRICE: Set the price of a NFT");
        System.out.println("\n SEARCH_NFT: Search for a NFT by name");
        System.out.println("\n BUY_NFT: Buy a NFT");
        System.out.println("\n");

        while (true) {
            String cmd = console.readLine("\n  > ");

            if (cmd.equalsIgnoreCase("MY_COINS")) {
                ArrayList<Coin> coins = bftDti.myCoins();
                if (coins.size() == 0){
                    System.out.println("You have no coins");
                    continue;
                } else{
                    System.out.println("\nYour coins:\n");
                
                    for(Coin coin : coins){
                        System.out.println("Coin id: " + coin.getId() + " Value: " + coin.getValue());
                    }
                }
                
            }

            else if (cmd.equalsIgnoreCase("MINT")){
                if (clientId == 4){
                    float value;
                    try{
                        value = Float.parseFloat(console.readLine("Enter value of coin: "));
                    } catch (NumberFormatException e){
                        System.out.println("Invalid value");
                        continue;
                    }
                    int createdCoinId = bftDti.mint(value);
                    if (createdCoinId >= 0){
                        System.out.println("Successfully created coin with id: " + createdCoinId);
                    }
                }
                else{
                    System.out.println("You dont have permission to do this operation");
                }
            }  

            else if(cmd.equalsIgnoreCase("SPEND")) {

                ArrayList<Coin> coins = bftDti.myCoins();
                if (coins.size() == 0){
                    System.out.println("You have no coins");
                    continue;
                } else{
                    System.out.println("\nYour coins:\n");
                
                    for(Coin coin : coins){
                        System.out.println("Coin id: " + coin.getId() + " Value: " + coin.getValue());
                    }
                }
                System.out.println("\n");
                String coinsToSpend = "";
                int receiverId;
                float value;
                String regex = "^\\d+(,\\d+)*$";
                try {
                    boolean goodInput=false;
                    while(goodInput==false){
                        coinsToSpend = console.readLine("Please enter the id of the coins you want to spend separated by commas (eg:1,2,3)\n  => ");
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(coinsToSpend);
                        goodInput = matcher.matches() || (coinsToSpend.length()==1 && coinsToSpend.matches("\\d")) ;
                    }
                    receiverId = Integer.parseInt(console.readLine("Enter receiver id: "));
                    value = Float.parseFloat(console.readLine("Enter value of transaction: "));
                } catch (Exception e) {
                    System.out.println("Invalid value");
                    continue;
                }
                String [] coinsToSpendArray = coinsToSpend.split(",");
                int spentCoinId = bftDti.spend(coinsToSpendArray,receiverId,value);
                if (spentCoinId==0) {
                    System.out.println("No coin was created (no money left in the coins)");
                }
                else if(spentCoinId==-1){
                    System.out.println("Operation Failed");
                }
                else if(spentCoinId==-2){
                    System.out.println("Operation failed, please do not insert the same coin id more than once");
                }
                else if(spentCoinId==-3){
                    System.out.println("Operation failed, the ids you provided do not match any of your coins");
                }
                else if(spentCoinId==-4){
                    System.out.println("Operation failed, not enough value in the coins");
                }
                else if(spentCoinId==-5){
                    System.out.println("Operation failed, receiver id cannot be the your id and it cannot be the ids of the replicas (0, 1, 2, 3)");
                }
                else{
                    System.out.println("Coin was created with id "+ spentCoinId);
                }
            }

            else if(cmd.equalsIgnoreCase("MY_NFTS")) {
                ArrayList<NFT> nfts = bftDti.myNFTs();
                if (nfts.size() == 0){
                    System.out.println("You have no NFTs");
                    continue;
                } else{
                    System.out.println("\nYour NFTs:\n");
                
                    for(NFT nft : nfts){
                        System.out.println("NFT id: " + nft.getId() + " Name: " + nft.getName() + " URI: " + nft.getURI() + " Value: " + nft.getValue()); 
                    }
                }
            }

            else if(cmd.equalsIgnoreCase("MINT_NFT")) {
                String name;
                String url;
                float value;
                try{
                    name = console.readLine("Enter name of NFT: ");
                    url = console.readLine("Enter URL of NFT: ");
                    value = Float.parseFloat(console.readLine("Enter value of NFT: "));
                } catch (NumberFormatException e){
                    System.out.println("Invalid value");
                    continue;
                }

                int createdNFTId = bftDti.mintNFT(name, url, value);
                if (createdNFTId >= 0){
                    System.out.println("Successfully created NFT with id: " + createdNFTId);
                } else if (createdNFTId == -1){
                    System.out.println("NFT with this name already exists");
                }
            }

            else if(cmd.equalsIgnoreCase("SET_NFT_PRICE")) { 
                int nftId;
                float value;
                try{
                    nftId = Integer.parseInt(console.readLine("Enter id of NFT you want to change the value: "));
                    value = Float.parseFloat(console.readLine("Enter value of NFT: "));
                } catch (NumberFormatException e){
                    System.out.println("Invalid value");
                    continue;
                }

                int confirmation = bftDti.setNFTPrice(nftId, value);
                if (confirmation == 1){
                    System.out.println("Successfully set price of NFT");
                } else if (confirmation == 0){
                    System.out.println("NFT with this name does not exist or you are not the owner");
                }
            }

            else if(cmd.equalsIgnoreCase("SEARCH_NFT")) {
                String name;
                try{
                    name = console.readLine("Enter name of NFT: ");
                } catch (NumberFormatException e){
                    System.out.println("Invalid value");
                    continue;
                }
                ArrayList<NFT> foundNFT = bftDti.searchNFT(name);
                if (foundNFT.size() == 0) {
                    System.out.println("NFT not found");
                }
                else{
                    System.out.println("Found the NFT.");
                    for (NFT nft : foundNFT) {
                        System.out.println("  NFT attributes:");
                        System.out.println("    =>NFT id: " + nft.getId() + 
                                        "\n    =>Name: " + nft.getName() + 
                                        "\n    =>URI: " + nft.getURI() + 
                                        "\n    =>Value: " + nft.getValue());
                    }

                }

            }
            else if(cmd.equalsIgnoreCase("BUY_NFT")) { 

                ArrayList<Coin> coins = bftDti.myCoins();
                if (coins.size() == 0){
                    System.out.println("You have no coins");
                    continue;
                } else{
                    System.out.println("\nYour coins:\n");
                
                    for(Coin coin : coins){
                        System.out.println("Coin id: " + coin.getId() + " Value: " + coin.getValue());
                    }
                }
                System.out.println("\n");
                String coinsToSpend = "";
                String regex = "^\\d+(,\\d+)*$";
                try {
                    boolean goodInput=false;
                    while(goodInput==false){
                        coinsToSpend = console.readLine("Please enter the id of the coins you want to spend separated by commas (eg:1,2,3)\n  => ");
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(coinsToSpend);
                        goodInput = matcher.matches() || (coinsToSpend.length()==1 && coinsToSpend.matches("\\d")) ;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid value");
                    continue;
                }
                String [] coinsToSpendArray = coinsToSpend.split(",");
                int nftId;
                try{
                    nftId = Integer.parseInt(console.readLine("Enter id of NFT you want to buy: "));
                } catch (NumberFormatException e){
                    System.out.println("Invalid value");
                    continue;
                }
                int spentCoinId = bftDti.buyNFT(nftId, coinsToSpendArray);
                if (spentCoinId==0) {
                    System.out.println("No coin was created (no money left in the coins)");
                }
                else if(spentCoinId==-1){
                    System.out.println("Operation Failed");
                }
                else if(spentCoinId==-2){
                    System.out.println("Operation failed, the nft id you provided does not match any existent nft");
                }
                else if(spentCoinId==-3){
                    System.out.println("Operation failed, please do not insert the same coin id more than once");
                }
                else if(spentCoinId==-4){
                    System.out.println("Operation failed, the nft you are trying to buy is already owned by you");
                }
                else if(spentCoinId==-5){
                    System.out.println("Operation failed, the value of the coins you provided is not enough to buy the nft");
                }
                else{
                    System.out.println("Coin was created with id "+ spentCoinId);
                }
            }

            else {
                System.out.println("Invalid command");
            }
        }       
    }
}
