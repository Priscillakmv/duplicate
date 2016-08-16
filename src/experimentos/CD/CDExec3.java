package experimentos.CD;

import classificacao.ClusterizacaoIncrementalIngenua;
import Index.BlockIndex;
import classificacao.ClusterizacaoIngenuaLote;
import java.io.IOException;
import query.QueryExperimento;

/**
 * Caso Tradicional Esta classe realiza a clusterização ingênua em um resultado
 * de consulta. Não ocorre criação de índices nem acesso a índices Todo o
 * processamento é feito sem nenhum reúso. Roda com a base toda e insere tudo no
 * BI. Depois clusteriza tradicional, sem recuperar nada prévio
 *
 * @author Matthias Pohl, Uwe Draisbach
 */
public class CDExec3 {

    public static void main(String[] args) throws IOException {
        long tempoInicial = System.currentTimeMillis();
        QueryExperimento queryTradicional = new QueryExperimento();
        BlockIndex bi2 = queryTradicional.blocaConsultaCD(queryTradicional.carregaDadosCD());
        ClusterizacaoIngenuaLote cluster = new ClusterizacaoIngenuaLote(bi2);
        cluster.cluserizaTradicional_NaoAcessaBIeSI2(bi2);
        System.err.println(" tetsdfsdf " + bi2.getNumeroElementos());
        System.out.println(" Tempo para processar a abordagem tradicional com o algoritmo ingenuo " + (System.currentTimeMillis() - tempoInicial));
    }

}
