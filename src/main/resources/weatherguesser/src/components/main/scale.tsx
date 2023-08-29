import React, {useEffect, useRef, useState} from "react";
import {useHomeContext} from "../../contexts/HomeContext";
import {IScaleProps} from "../../interfaces/IProps";

const Scale: React.FC<IScaleProps> = () => {
    const {
        accuracy
    } = useHomeContext();
    const [accuracyDegrees, setAccuracyDegrees] = useState<number>(Math.PI + (Math.PI * accuracy / 100));
    useEffect(() => {
        setAccuracyDegrees(Math.PI + (Math.PI * accuracy / 100));
    }, [accuracy]);

    const canvasRef = useRef<HTMLCanvasElement | null>(null);
    let startAngle = Math.PI;
    let endAngle = Math.PI;
    let animationFrameId: number = 0;

    const animate = () => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext("2d");
        if (!ctx) return;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        endAngle += 0.03;

        const gradient = ctx.createLinearGradient(
            Math.cos(startAngle) * 210 + 210,
            Math.sin(startAngle) * 105 + 105,
            420,
            210
        );

        gradient.addColorStop(0, 'rgba(245, 245, 245, 1)');
        gradient.addColorStop(1, 'rgba(46, 128, 176, 1)');

        ctx.strokeStyle = gradient;
        ctx.lineWidth = 20;
        ctx.lineCap = 'round';
        ctx.shadowColor = 'rgba(0, 0, 0, 0.4)';
        ctx.shadowBlur = 10;
        ctx.shadowOffsetX = 5;
        ctx.shadowOffsetY = 5;
        ctx.beginPath();
        ctx.arc(220, 210, 200, startAngle, endAngle);
        ctx.stroke();

        if (endAngle < accuracyDegrees) {
            animationFrameId = requestAnimationFrame(animate);
        }
    };

    useEffect(() => {
        const canvas = canvasRef.current;
        if (canvas) animate();

        return () => {
            cancelAnimationFrame(animationFrameId);
        };
    }, [accuracyDegrees]);

    return <canvas ref={canvasRef} width={430} height={235} className="z-10" />;
}

export default Scale;
