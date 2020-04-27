package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.chess.domain.Position;

public class KingTest {

    @ParameterizedTest
    @DisplayName("킹이 움직일 수 없는 위치가 들어갔을 때 예외를 잘 처리하는지 확인")
    @CsvSource(value = {"a3", "e5"})
    void invalidKingMove(String destination) {
        King king = new King(new Position("e1"), Team.WHITE);
        assertThatThrownBy(() ->
            king.validateMove(new Position(destination))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("킹이 제대로 움직이는지 확인")
    @CsvSource(value = {"e1", "e3", "d1", "d2", "d3", "f1", "f2", "f3"})
    void kingMove(String destination) {
        King king = new King(new Position("e2"), Team.WHITE);
        king.move(new Position(destination));
    }

}
