package wooteco.chess.domain.piece;

import java.util.Arrays;

import wooteco.chess.domain.Position;
import wooteco.chess.exception.IllegalMoveException;

public class Queen extends Piece {
    public Queen(Position position, Team team) {
        super(position, team);
        this.representation = Arrays.asList('♕', '♛');
        this.score = PieceRule.QUEEN.getScore();
    }

    @Override
    public void validateMove(Position destination) {
        if (this.position.isNonDiagonalDirection(destination) && this.position.isNonLinearDirection(
            destination)) {
            throw new IllegalMoveException(ILLEGAL_MOVE);
        }
    }
}
