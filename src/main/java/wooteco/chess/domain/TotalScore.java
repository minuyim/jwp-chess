package wooteco.chess.domain;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRule;

public class TotalScore {
    private static final double PAWN_DISADVANTAGE = 0.5;
    private final List<Piece> pieces;
    private final double totalScore;

    public TotalScore(List<Piece> pieces) {
        this.pieces = pieces;
        this.totalScore = calculateTotalScore();
    }

    private double calculateTotalScore() {
        double plainSum = pieces.stream().mapToDouble(Piece::getScore).sum();
        return sameColumnPawnHandler(plainSum);
    }

    private double sameColumnPawnHandler(double plainSum) {
        List<Pawn> pawns = pieces.stream()
            .filter(p -> p.getScore() == PieceRule.PAWN.getScore())
            .map(p -> (Pawn)p)
            .collect(Collectors.toList());
        for (Pawn pawn : pawns) {
            if (pawns.stream().anyMatch(p -> !p.equals(pawn) && p.isInSameColumn(pawn))) {
                plainSum -= PAWN_DISADVANTAGE;
            }
        }
        return plainSum;
    }

    public double getTotalScore() {
        return totalScore;
    }
}
