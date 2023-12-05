package br.com.caluan.integracao.diversas.processor;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeitorCsvProcessor {

    public LeitorCsvProcessor() {
    }

    //Este exemplo ele Le conforme o input de dados, colcoar em um Spring boot para execução normal e importar na mão
    //Ou mudar o tipo do input
    public void leArquivo() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo CSV para conversão");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File inputFile = fileChooser.getSelectedFile();
            BufferedWriter writer = null ;

            try {

                CSVReader reader = new CSVReaderBuilder(new FileReader(inputFile))
                        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                        .build();


                String[] nextRecord;
                SimpleDateFormat dateFormatData = new SimpleDateFormat("ddMMyyyy_HHmmss");
                SimpleDateFormat dateFormatHora = new SimpleDateFormat("HH:mm:ss");

                // Ignora as 5 primeiras linhas
                for (int x = 0; x < 5; x++) {
                    reader.readNext();
                }

                while ((nextRecord = reader.readNext()) != null) {

                    String codigoEmpresa = "00000000";
                    String codigoEstabelecimento = nextRecord[28].replaceAll("\\D", "");
                    String nsuSiTef = nextRecord[5];
                    String identificacaoPDV = nextRecord[3];
                    String nsuAdministradora = nextRecord[4];
                    String estadoTransacao = nextRecord[17].equals("Efetuada PDV") ? "EFETUADA" : "";
                    String codigoTransacao = getEstadotransacao(nextRecord[15].toString());
                    String codigoResposta = (nextRecord[17].toString().equals("Efetuada PDV") ? "000" : "001");
                    String dataHoraString = nextRecord[0];
                    String numeroCartao = nextRecord[2];
                    String valorCompra = nextRecord[6].replace(",", ".");
                    String numeroParcelas = nextRecord[16];
                    String codigoAutorizacao = nextRecord[10];
                    String nomeProduto = nextRecord[14];
                    String redeAutorizadora = getRedeAutorizadora(nextRecord[8]);
                    String numeroLoja = nextRecord[1];

                    // Exemplo de gravar os dados em um novo arquivo

                    if(writer == null){

                        Date data = new Date();
                        String outputFile = inputFile.getParent()+"\\TRANSACOES_"+codigoEstabelecimento+
                                "_"+dateFormatData.format(data).toString()+".csv";

                        writer = writer = new BufferedWriter(new FileWriter(outputFile));
                    }


                    if (!(codigoTransacao.equals("ABERTERM") || codigoTransacao.equals("MOEDADCC"))) {
                        String linha = "\"" +
                                codigoEmpresa + "\";\"" +
                                codigoEstabelecimento + "\";\"" +
                                nsuSiTef + "\";\"" +
                                identificacaoPDV + "\";\"" +
                                nsuAdministradora + "\";\"" +
                                estadoTransacao + "\";\"" +
                                codigoTransacao + "\";\"" +
                                codigoResposta + "\";\"" +
                                dataHoraString.substring(0, dataHoraString.indexOf(" ")) + "\";\"" +
                                dataHoraString.substring(dataHoraString.indexOf(" ") +1,dataHoraString.length()) + "\";\"" +
                                numeroCartao + "\";" +
                                valorCompra.replace(".", ",") + ";" +
                                numeroParcelas + ";\"" +
                                codigoAutorizacao + "\";\"" +
                                nomeProduto + "\";\"" +
                                redeAutorizadora + "\";\"" +
                                numeroLoja + "\";";


                        writer.write(linha);
                        writer.newLine();
                    }
                }

                writer.close();

                System.out.println("Conversão concluída com sucesso.");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static String getEstadotransacao(String estado) {
        if (estado == null) {
            return "";
        }

        switch (estado) {
            case "Abertura Pdv":
                return "ABERTERM";
            case "Compra Pix":
                return "CMPCARTD";
            case "Credito a Vista":
                return "CMPCTCRM";
            case "Credito Parcelado sem Juros":
                return "CMPCRNJU";
            case "Debito a Vista":
                return "CMPCTDEB";
            case "Consulta Conv Moeda DCC":
                return "MOEDADCC";

            default:
                return estado;
        }

    }

    private static String getRedeAutorizadora(String autorizadora) {
        if (autorizadora == null) {
            return "";
        }

        switch (autorizadora.toUpperCase()) {
            case "VISA CREDITO":
            case "VISA ELECTRON":
                return "VISA4";
            case "MASTERCARD":
                return "MASTERCARD";
            case "MAESTRO":
                return "MAESTRO";
            case "ELO DEBITO":
                return "ELO";
            case "REDE":
                return "REDECARD";
            default:
                return autorizadora.toUpperCase();
        }

    }
}
