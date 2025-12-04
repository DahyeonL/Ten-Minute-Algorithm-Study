import React, { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import './worldmap.css';

export type StageStatus = 'LOCKED' | 'UNLOCKED' | 'CLEARED';

export interface AreaDto {
  id: number;
  name: string;
  description?: string;
  orderIndex: number;
  posX: number;
  posY: number;
}

export interface StageWithProgressDto {
  id: number;
  areaId: number;
  name: string;
  description: string;
  difficulty: number;
  status: StageStatus;
  problemType: string;
  posX: number;
  posY: number;
}

export interface StageDetailResponse {
  id: number;
  areaId: number;
  name: string;
  description: string;
  difficulty: number;
  problemType: string;
  inputSpec: string;
  outputSpec: string;
  exampleInput: string;
  exampleOutput: string;
  status: StageStatus;
}

type StageNodeProps = {
  stage: StageWithProgressDto;
  onClick: () => void;
};

const StageNode: React.FC<StageNodeProps> = ({ stage, onClick }) => {
  const handleClick = stage.status === 'LOCKED' ? undefined : onClick;

  return (
    <div
      className={`stage-node stage-${stage.status.toLowerCase()}`}
      style={{ left: stage.posX, top: stage.posY, position: 'absolute' }}
      onClick={handleClick}
      role="button"
      aria-label={stage.name}
    >
      <div className="stage-label">{stage.name}</div>
      <div className="stage-description">{stage.description}</div>
      <div className="stage-difficulty">{'★'.repeat(stage.difficulty)}</div>
    </div>
  );
};

type MapCanvasProps = {
  areas: AreaDto[];
  stages: StageWithProgressDto[];
  onStageClick: (stageId: number) => void;
};

const MapCanvas: React.FC<MapCanvasProps> = ({ areas, stages, onStageClick }) => {
  return (
    <div className="map-canvas">
      {areas.map((area) => (
        <div
          key={area.id}
          className="area-node"
          style={{ left: area.posX, top: area.posY, position: 'absolute' }}
        >
          <div className="area-name">{area.name}</div>
          <p className="area-description">{area.description}</p>
        </div>
      ))}

      {stages.map((stage) => (
        <StageNode key={stage.id} stage={stage} onClick={() => onStageClick(stage.id)} />
      ))}
    </div>
  );
};

type StageDetailPanelProps = {
  stageDetail: StageDetailResponse | null;
  onSubmit: (code: string) => Promise<void>;
  isLoading: boolean;
};

const StageDetailPanel: React.FC<StageDetailPanelProps> = ({ stageDetail, onSubmit, isLoading }) => {
  const [code, setCode] = useState('');
  const [submitResult, setSubmitResult] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    setCode('');
    setSubmitResult(null);
  }, [stageDetail?.id]);

  const handleSubmit = async () => {
    if (!stageDetail) return;
    setIsSubmitting(true);
    setSubmitResult(null);
    try {
      await onSubmit(code);
      setSubmitResult('제출이 완료되었습니다!');
    } catch (err) {
      setSubmitResult('제출에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!stageDetail) {
    return <div className="stage-detail-panel empty">스테이지를 선택해주세요.</div>;
  }

  return (
    <div className="stage-detail-panel">
      <header>
        <h2>{stageDetail.name}</h2>
        <div className="stage-meta">
          <span className="difficulty">{'★'.repeat(stageDetail.difficulty)}</span>
          <span className="problem-type">{stageDetail.problemType}</span>
          <span className={`status-badge status-${stageDetail.status.toLowerCase()}`}>{stageDetail.status}</span>
        </div>
      </header>

      <p className="stage-description">{stageDetail.description}</p>

      <div className="io-specs">
        <div>
          <h4>Input</h4>
          <pre>{stageDetail.inputSpec}</pre>
        </div>
        <div>
          <h4>Output</h4>
          <pre>{stageDetail.outputSpec}</pre>
        </div>
      </div>

      <div className="examples">
        <div>
          <h4>예시 입력</h4>
          <pre>{stageDetail.exampleInput}</pre>
        </div>
        <div>
          <h4>예시 출력</h4>
          <pre>{stageDetail.exampleOutput}</pre>
        </div>
      </div>

      <div className="code-editor">
        <h4>코드 작성</h4>
        <textarea
          placeholder="여기에 코드를 입력하세요"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          rows={10}
        />
        <button onClick={handleSubmit} disabled={isSubmitting || isLoading || stageDetail.status === 'LOCKED'}>
          {isSubmitting ? '제출 중...' : '제출'}
        </button>
        {submitResult && <div className="submit-result">{submitResult}</div>}
      </div>
    </div>
  );
};

export const WorldMapPage: React.FC = () => {
  const userId = 'demo-user';
  const [areas, setAreas] = useState<AreaDto[]>([]);
  const [stages, setStages] = useState<StageWithProgressDto[]>([]);
  const [selectedStage, setSelectedStage] = useState<StageDetailResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchWorldMap = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await axios.get('/api/worldmap', {
          params: { userId },
        });

        setAreas(response.data.areas);
        setStages(response.data.stages);
      } catch (err) {
        setError('월드맵을 불러오는데 실패했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchWorldMap();
  }, [userId]);

  const handleStageClick = async (stageId: number) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await axios.get(`/api/stages/${stageId}`, {
        params: { userId },
      });
      setSelectedStage(response.data);
    } catch (err) {
      setError('스테이지 정보를 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (code: string) => {
    if (!selectedStage) return;
    await axios.post(`/api/stages/${selectedStage.id}/submit`, { code });
  };

  const selectedStageId = useMemo(() => selectedStage?.id ?? null, [selectedStage]);

  return (
    <div className="world-map-page">
      <div className="map-section">
        <div className="section-header">
          <h1>알고리즘 퍼즐 월드맵</h1>
          {isLoading && <span className="loading">불러오는 중...</span>}
        </div>
        {error && <div className="error">{error}</div>}
        <MapCanvas areas={areas} stages={stages} onStageClick={handleStageClick} />
      </div>

      <div className="detail-section">
        <StageDetailPanel stageDetail={selectedStage} onSubmit={handleSubmit} isLoading={isLoading} />
        {!selectedStageId && <div className="hint">스테이지를 클릭하여 상세 정보를 확인하세요.</div>}
      </div>
    </div>
  );
};

export default WorldMapPage;
