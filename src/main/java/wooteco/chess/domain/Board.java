package wooteco.chess.domain;

import java.util.List;
import java.util.Objects;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.piece.PieceRule;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.exception.IllegalMoveException;
import wooteco.chess.exception.NullPieceException;

public class Board {
    private static final String SOURCE_SAME_WITH_DESTINATION = "말이 원래 있던 자리입니다.";
    private static final String NO_PIECE_IN_SOURCE = "해당 위치에 말이 없습니다.";
    private static final String UNMOVABLE_DESTINATION_FOR_PAWN = "폰이 이동할 수 없는 위치입니다.";
    private static final String PIECE_IN_PATH = "경로에 다른 말이 있어 움직일 수 없습니다.";
    private static final String SAME_TEAM_PIECE_IN_DESTINATION = "해당 자리에 같은 팀 말이 있기 때문에 말을 움직일 수 없습니다!";

    private final Pieces pieces;
    private Team turn;

    public Board() {
        PieceFactory pieceFactory = PieceFactory.create();
        this.pieces = new Pieces(pieceFactory.getPieces());
        this.turn = Team.WHITE;
    }

    public Board(Pieces pieces, Team turn) {
        this.pieces = pieces;
        this.turn = turn;
    }

    public void movePiece(Position source, Position destination) {
        validateTurn(source, turn);
        validateDestination(source, destination);
        Piece sourcePiece = pieces.findByPosition(source);
        Piece destinationPiece = pieces.findByPosition(destination);
        if (sourcePiece.getScore() == PieceRule.PAWN.getScore()) {
            validatePawnDestination(source, destination);
        }
        if (!(sourcePiece.getScore() == PieceRule.KNIGHT.getScore())) {
            validateNoObstacle(source, destination);
        }
        if (destinationPiece != null) {
            killPiece(sourcePiece, destinationPiece);
        }
        pieces.move(source, destination);
        this.turn = turn.changeTurn();
    }

    private void validateTurn(Position source, Team turn) {
        Piece sourcePiece = pieces.findByPosition(source);
        validateSource(sourcePiece);
        if (sourcePiece.getTeam() != turn) {
            throw new IllegalMoveException(turn.getName() + "의 차례입니다.");
        }
    }

    private void validateDestination(Position source, Position destination) {
        if (source.equals(destination)) {
            throw new IllegalMoveException(SOURCE_SAME_WITH_DESTINATION);
        }
    }

    private void validateSource(Piece piece) {
        if (piece == null) {
            throw new NullPieceException(NO_PIECE_IN_SOURCE);
        }
    }

    private void validatePawnDestination(Position source, Position destination) {
        Direction direction = source.calculateDirection(destination);
        if (direction.isForwardForPawn() && pieces.findByPosition(destination) != null) {
            throw new IllegalMoveException(UNMOVABLE_DESTINATION_FOR_PAWN);
        }
        if (direction.isDiagonal() && pieces.findByPosition(destination) == null) {
            throw new IllegalMoveException(UNMOVABLE_DESTINATION_FOR_PAWN);
        }
    }

    private void validateNoObstacle(Position source, Position destination) {
        List<Position> positionsInBetween = source.getPositionsInBetween(destination);
        for (Position position : positionsInBetween) {
            if (pieces.findByPosition(position) != null) {
                throw new IllegalMoveException(PIECE_IN_PATH);
            }
        }
    }

    private void killPiece(Piece piece, Piece destinationPiece) {
        if (piece.isSameTeam(destinationPiece)) {
            throw new IllegalMoveException(SAME_TEAM_PIECE_IN_DESTINATION);
        }
        pieces.kill(destinationPiece);
    }

    public double calculateScoreByTeam(Team team) {
        return new TotalScore(pieces.getAlivePiecesByTeam(team)).getTotalScore();
    }

    public boolean isBothKingAlive() {
        return pieces.isBothKingAlive();
    }

    public Pieces getPieces() {
        return pieces;
    }

    public Team getWinner() {
        return pieces.teamWithAliveKing();
    }

    public Team getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Board board = (Board)o;
        return Objects.equals(pieces, board.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }
}
