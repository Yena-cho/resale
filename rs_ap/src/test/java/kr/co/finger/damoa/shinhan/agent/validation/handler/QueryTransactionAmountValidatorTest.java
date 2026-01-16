package kr.co.finger.damoa.shinhan.agent.validation.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryTransactionAmountValidatorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTransactionAmountValidatorTest.class);
    
    @Test
    public void createMatrix() {
        int column=3;
        int row= 1 << column;
        RealMatrix matrix = MatrixUtils.createRealMatrix(row, column);
        
        for(int i=0; i<row; i++) {
            final String maze = StringUtils.leftPad(Integer.toBinaryString(i), column, '0');
            for(int j=0; j<maze.length(); j++) {
                int value = NumberUtils.toInt(StringUtils.substring(maze, j, j+1), 0);
                LOGGER.debug("value: {}", value);
                matrix.setEntry(i, j, value);
            }
            LOGGER.info("{}", maze);
        }
        
        LOGGER.info("{}", matrix);
        
        RealMatrix o = MatrixUtils.createRealMatrix(new double[][]{new double[]{1000}, new double[]{2000}, new double[]{300}});

        RealMatrix r = matrix.multiply(o);
        
        LOGGER.info("r: {}", r);
    }
}
