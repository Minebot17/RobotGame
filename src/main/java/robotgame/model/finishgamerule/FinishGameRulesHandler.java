package robotgame.model.finishgamerule;

import robotgame.model.HexagonField;

import java.util.List;

public class FinishGameRulesHandler {

    private final List<RuleParameters> gameRulesParameters;
    private boolean isGameOver;
    private boolean isPlayerWin;

    public FinishGameRulesHandler(HexagonField field, List<RuleParameters> gameRulesParameters){
        this.gameRulesParameters = gameRulesParameters;

        for (RuleParameters gameRulesParameter : gameRulesParameters){
            gameRulesParameter.initialize(field);
        }
    }

    public void updateGameState(){
        if (isGameOver){
            return;
        }

        RulesInfo rulesInfo = new RulesInfo(gameRulesParameters);
        handleGameStateResult(rulesInfo.updateRulesAndCountIndependent());
        handleGameStateResult(rulesInfo.handleUpdatedRules());
    }

    public boolean isGameOver(){
        return isGameOver;
    }

    public boolean isPlayerWin(){
        return isPlayerWin;
    }

    private void handleGameStateResult(GameStateResult gameStateResult){
        if (gameStateResult == GameStateResult.None){
            return;
        }

        isGameOver = true;
        this.isPlayerWin = gameStateResult == GameStateResult.PlayerWin;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (RuleParameters parameters : gameRulesParameters) {
            if (parameters.ruleLinkType == RuleLinkType.OR){
                continue;
            }

            if (result.length() != 0) {
                result.append(" И ");
            }

            if (parameters.completeConditionsIsNegative){
                result.append("НЕ ");
            }

            result.append(parameters.rule.toString());
        }

        for (RuleParameters parameters : gameRulesParameters) {
            if (parameters.ruleLinkType != RuleLinkType.OR) {
                continue;
            }

            if (result.length() != 0) {
                result.append(" ИЛИ ");
            }

            if (parameters.completeConditionsIsNegative){
                result.append("НЕ ");
            }

            result.append(parameters.rule.toString());
        }

        return result.toString();
    }

    public static class RuleParameters {

        private FinishGameRuleFactory ruleFactory;
        private FinishGameRule rule;

        public final boolean completeConditionsIsNegative;
        public final RuleLinkType ruleLinkType;

        public RuleParameters(FinishGameRuleFactory ruleFactory, boolean completeConditionsIsNegative, RuleLinkType ruleLinkType) {
            this.ruleFactory = ruleFactory;
            this.completeConditionsIsNegative = completeConditionsIsNegative;
            this.ruleLinkType = ruleLinkType;
        }

        public void initialize(HexagonField field){
            rule = ruleFactory.create(field);
            ruleFactory = null;
        }

        public String toStringRule(){
            return rule == null ? ruleFactory.create(null).toString() : rule.toString();
        }
    }

    private class RulesInfo {

        private final List<RuleParameters> gameRulesParameters;

        public boolean haveNotIndependent;
        public boolean notIndependentComplete;
        public boolean notIndependentFail;

        public int independentCount;
        public int failIndependentCount;

        public RulesInfo(List<RuleParameters> gameRulesParameters) {
            this.gameRulesParameters = gameRulesParameters;
        }

        public GameStateResult updateRulesAndCountIndependent(){
            for (RuleParameters parameters : gameRulesParameters) {
                parameters.rule.updateGameState();

                boolean isCompleteConditionsMet = parameters.rule.isCompleteConditionsMet();
                boolean isFailConditionsMet = parameters.rule.isFailConditionsMet();

                if (parameters.completeConditionsIsNegative){
                    isCompleteConditionsMet = !isCompleteConditionsMet;
                }

                if (parameters.ruleLinkType == RuleLinkType.AND){
                    handleAndRule(parameters, isCompleteConditionsMet, isFailConditionsMet);
                }
                else {
                    if (isCompleteConditionsMet){
                        return GameStateResult.PlayerWin;
                    }

                    handleOrRule(parameters, isFailConditionsMet);
                }
            }

            return GameStateResult.None;
        }

        private void handleAndRule(RuleParameters parameters, boolean isCompleteConditionsMet, boolean isFailConditionsMet){
            haveNotIndependent = true;
            notIndependentComplete &= isCompleteConditionsMet;

            if (!parameters.completeConditionsIsNegative) {
                notIndependentFail |= isFailConditionsMet;
            }
        }

        private void handleOrRule(RuleParameters parameters, boolean isFailConditionsMet){
            independentCount++;

            if (!parameters.completeConditionsIsNegative && isFailConditionsMet){
                failIndependentCount++;
            }
        }

        public GameStateResult handleUpdatedRules(){
            if (haveNotIndependent){
                if (notIndependentComplete){
                    return GameStateResult.PlayerWin;
                }

                if (notIndependentFail){
                    if (independentCount == 0){
                        return GameStateResult.PlayerLose;
                    }

                    failIndependentCount++;
                }

                independentCount++;
            }

            if (independentCount == failIndependentCount){
                return GameStateResult.PlayerLose;
            }

            return GameStateResult.None;
        }
    }

    private enum GameStateResult {
        None, PlayerWin, PlayerLose
    }
}
