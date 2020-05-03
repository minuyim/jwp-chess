package wooteco.chess.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRule;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.repository.PieceEntity;

public class Pieces {
    private static final int BOTH_TEAM_KING_NUMBER = 2;

    private final Map<Position, Piece> pieces;

    public Pieces(Map<Position, Piece> pieces) {
        this.pieces = new HashMap<>(pieces);
    }

    public List<Piece> getAlivePieces() {
        return pieces.values().stream()
            .filter(Piece::isAlive)
            .collect(Collectors.toList());
    }

    public List<Piece> getAlivePiecesByTeam(Team team) {
        return this.getAlivePieces().stream()
            .filter(p -> p.isInTeam(team))
            .collect(Collectors.toList());
    }

    public Piece findByPosition(Position position) {
        return pieces.get(position);
    }

    public boolean isBothKingAlive() {
        return this.getAlivePieces().stream()
            .filter(p -> p.getScore() == PieceRule.KING.getScore())
            .count() == BOTH_TEAM_KING_NUMBER;
    }

    public Team teamWithAliveKing() {
        return this.getAlivePieces().stream()
            .filter(p -> p.getScore() == PieceRule.KING.getScore())
            .findFirst()
            .orElseThrow(() -> new NullPointerException("킹이 한 명도 없습니다."))
            .getTeam();
    }

    public void move(Position source, Position destination) {
        Piece sourcePiece = this.pieces.get(source);
        sourcePiece.move(destination);
        this.pieces.put(destination, sourcePiece);
        this.pieces.remove(source);
    }

    public void kill(Piece destinationPiece) {
        destinationPiece.kill();
        this.pieces.values().remove(destinationPiece);
    }

    public Map<Position, Piece> getPieces() {
        return pieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pieces pieces1 = (Pieces)o;
        return Objects.equals(pieces, pieces1.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }
}
