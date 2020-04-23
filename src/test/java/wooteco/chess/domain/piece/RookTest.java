package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.chess.domain.Position;

public class RookTest {

    @ParameterizedTest
    @DisplayName("룩이 움직일 수 없는 위치가 들어갔을 때 예외를 잘 처리하는지 확인")
    @CsvSource(value = {"a3", "h5"})
    void invalidRookMove(String destination) {
        Rook rook = new Rook(new Position("e4"), Team.WHITE);
        assertThatThrownBy(() ->
            rook.validateMove(new Position(destination))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("룩이 제대로 움직이는지 확인")
    @CsvSource(value = {"e1", "e3", "e8", "b4", "c4", "h4"})
    void rookMove(String destination) {
        Rook rook = new Rook(new Position("e4"), Team.WHITE);
        rook.move(new Position(destination));
    }
}
